/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.handlers.ServerHandler;
import com.steve6472.multiplayerTest.network.packets.CPacket;
import com.steve6472.multiplayerTest.network.packets.server.*;
import com.steve6472.multiplayerTest.network.packets.server.world.SInitClientData;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.networking.UDPServer;
import com.steve6472.sge.main.networking.packet.IPacketHandler;
import com.steve6472.sge.main.networking.packet.Packet;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.World;

public class Server extends UDPServer
{

	public ServerGui sg;
	public static final int moveLimit = 64;

	/* Kick player after --- ms of inactivity */
	public static final long timeout = 10 * 1000;

	public static int nextNetworkId;

	public IObjectManipulator<Bullet> bullets;

	/* Lag-meter */
	public static long updateTime = 0;
	
	public static final byte lagChunkDelay = 15;

	public Server(int port, ServerGui sg)
	{
		super(port);
		setIPacketHandler(new ServerHandler(this, sg));
		this.sg = sg;
		bullets = new IObjectManipulator<Bullet>();
	}

	public void tick()
	{
		long tickStart = System.currentTimeMillis();
		bullets.tick(true);
		for (Bullet b : bullets.getAll())
		{
			b.tick(sg, this);
		}

		for (Iterator<PlayerMP> iter = sg.players.iterator(); iter.hasNext();)
		{
			PlayerMP p = iter.next();

			if (System.currentTimeMillis() - p.lastUpdate > timeout)
			{
				String text = p.getIp() + ":" + p.getPort();
				int remove = -1;

				for (int i = 0; i < sg.playerList.getItems().size(); i++)
				{
					if (sg.playerList.getItems().get(i).getText().equals(text))
					{
						remove = i;
						break;
					}
				}

				if (remove != -1)
				{
					sendPacket(new SDisconnectPlayer(sg.players.get(remove)));
					sg.playerList.removeItem(remove);
					iter.remove();
				}
			}
		}

		for (PlayerMP p : sg.players)
		{
			p.tick();
		}

		updateTime = System.currentTimeMillis() - tickStart;
	}

	public boolean isTileLocOutOfBounds(int x, int y, ServerWorld world)
	{
		return (x < 0 || y < 0 || x >= (World.worldWidth * Chunk.chunkWidth) || y >= (World.worldHeight * Chunk.chunkHeight));
	}
	
	@Override
	public void clientConnectEvent(DatagramPacket packet)
	{
		sg.playerList.addItem(packet.getAddress() + ":" + packet.getPort());

		for (PlayerMP p : sg.players)
		{
			sendPacket(new SConnectPlayer(p), packet.getAddress(), packet.getPort());
		}

		PlayerMP newPlayer = addNewPlayer(packet.getAddress(), packet.getPort(), Game.spawnX, Game.spawnY);
		sendPacket(new SSetNetworkId(newPlayer.getNetworkId()), packet);
		sendPacket(new SInitClientData(World.worldWidth, World.worldHeight, Chunk.chunkWidth, Chunk.chunkHeight, Chunk.layerCount, ServerTile.getAtlas()), packet);
		sendPacket(new SChat("Player has connected", -1));

		sendPacketWithException(new SConnectPlayer(newPlayer), packet);
	}

	@Override
	public void clientDisconnectEvent(DatagramPacket packet)
	{
		String text = packet.getAddress() + ":" + packet.getPort();
		int remove = -1;

		for (int i = 0; i < sg.playerList.getItems().size(); i++)
		{
			if (sg.playerList.getItems().get(i).getText().equals(text))
			{
				remove = i;
				break;
			}
		}

		if (remove != -1)
		{
			sendPacket(new SDisconnectPlayer(sg.players.get(remove)));
			sg.playerList.removeItem(remove);
			sg.players.remove(remove);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handlePacket(Packet<?> packet, int packetId, DatagramPacket p)
	{
		if (packet instanceof CPacket)
		{
			CPacket sPacket = (CPacket) packet;
			sPacket.setSender(p);
			sPacket.handlePacket(this, sg);
		} else
		{
			packet.setSender(p);
			((Packet<IPacketHandler>)packet).handlePacket(packetHandler);
		}
	}
	
	public PlayerMP getPlayer(CPacket packet)
	{
		return getPlayer(packet.getSender());
	}

	public PlayerMP getPlayer(DatagramPacket playerIp)
	{
		for (PlayerMP p : sg.players)
		{
			if (p.getPort() == playerIp.getPort() && p.getIp().equals(playerIp.getAddress()))
			{
				return p;
			}
		}
		return null;
	}

	public PlayerMP getPlayer(int networkId)
	{
		for (PlayerMP p : sg.players)
		{
			if (p.getNetworkId() == networkId)
				return p;
		}
		return null;
	}

	public PlayerMP addNewPlayer(InetAddress ip, int port, int x, int y)
	{
		PlayerMP player = new PlayerMP(ip, port, x, y, this);
		player.updateBox();
		player.worldId = 0;
		player.visitedChunks = new byte[World.worldWidth * World.worldHeight];
		sg.players.add(player);

		for (int i = 0; i < player.renderDistance; i++)
		{
			for (int j = 0; j < player.renderDistance; j++)
			{
				int X = Util.getNumberBetween(0, World.worldWidth, player.getChunkX() + i - 1);
				int Y = Util.getNumberBetween(0, World.worldHeight, player.getChunkY() + j - 1);

				if (X >= 0 && Y >= 0 && X < World.worldWidth && Y < World.worldHeight)
				{
					byte b = player.visitedChunks[X + Y * World.worldWidth];
					if (b == 0)
					{
						player.visitedChunks[X + Y * World.worldWidth] = lagChunkDelay;
						player.tickVCH.addObject(X + Y * World.worldWidth);
					}
				}
			}
		}
		
		return player;
	}
	
	public SGArray<PlayerMP> getPlayersInRange(int tx, int ty, int range)
	{
		SGArray<PlayerMP> players = new SGArray<PlayerMP>();
		
		for (PlayerMP p : sg.players)
		{
			int ptx = p.getX() / 32;
			int pty = p.getY() / 32;
			if (Util.getDistance(ptx, pty, tx, ty) <= range)
			{
				players.addObject(p);
			}
		}
		
		return players;
	}
	
	public List<PlayerMP> getPlayers()
	{
		return sg.players;
	}
	
	public ServerWorld getWorld()
	{
		return sg.world0;
	}
}

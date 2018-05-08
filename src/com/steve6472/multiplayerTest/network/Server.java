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
import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.ServerGui;
import com.steve6472.multiplayerTest.network.handlers.ServerHandler;
import com.steve6472.multiplayerTest.network.packets.server.*;
import com.steve6472.multiplayerTest.network.packets.server.world.SInitClientData;
import com.steve6472.multiplayerTest.network.packets.server.world.SSetChunk;
import com.steve6472.multiplayerTest.server.events.BuildStructure;
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

	ServerGui sg;
	public static final int moveLimit = 64;

	/* Kick player after --- ms of inactivity */
	public static final long timeout = 10 * 1000;

	public static int nextNetworkId;

	public IObjectManipulator<Bullet> bullets;

	/* Lag-meter */
	public static long updateTime = 0;
	
	private static final byte lagChunkDelay = 15;

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
			if (b == null)
				continue;
			for (PlayerMP p : sg.players)
			{
				
				if (b.getShooterNetworkId() == p.getNetworkId())
					continue;

				if (b.getBox().intersects(p.getBox()))
				{
					p.score -= 2;
					if (p.score < 0)
						p.score = 0;

					sendPacket(new SSetScore(p.score, p.getNetworkId()));

					PlayerMP shooter = getPlayer(b.getShooterNetworkId());

					if (shooter != null)
						sendPacket(new SSetScore(shooter.score(), b.getShooterNetworkId()));
					else
						System.err.println("Can't find player with networkId " + b.getShooterNetworkId());

					sendPacket(new SDeleteBullet(b.getNetworkId()));
					b.setDead();
				}
			}

			int bulletTileX = b.getLocation().getIntX() / 32;
			int bulletTileY = b.getLocation().getIntY() / 32;

			if (isTileLocOutOfBounds(bulletTileX, bulletTileY, sg.world0))
			{
				sendPacket(new SDeleteBullet(b.getNetworkId()));
				b.setDead();
				continue;
			}

			if (ServerTile.getTile(sg.world0.getTileInWorld(bulletTileX, bulletTileY, 0)).isSolid())
			{
				sendPacket(new SDeleteBullet(b.getNetworkId()));

				int id = ServerTile.getTile(sg.world0.getTileInWorld(bulletTileX, bulletTileY, 0)).getId();
				ServerTile.getTile(id).bulletCollision(bulletTileX, bulletTileY, b, sg.world0);
				b.setDead();
			}
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
			if (!p.checkLocation)
				continue;

			int px = p.getX() - 16;
			int py = p.getY() - 16;
			
			int cx = p.getChunkX();
			int cy = p.getChunkY();
			
			tickRemove(p);
			tickChunks(p, cx, cy);
			
			int s = 4;

			int px00 = (px + s) / 32;
			int py00 = (py + s) / 32;

			int px10 = (px + 32 - s) / 32;
			int py10 = (py + s) / 32;

			int px01 = (px + s) / 32;
			int py01 = (py + 32 - s) / 32;

			int px11 = (px + 32 - s) / 32;
			int py11 = (py + 32 - s) / 32;

			boolean isValid = true;

			if (!isTileLocOutOfBounds(px00, py00, sg.getWorld(p.worldId)))
				if (ServerTile.getTile(getPlayersWorld(p).getTileInWorld(px00, py00, 0)).isSolid())
					isValid = moveToLastValidLocation(p);

			if (!isTileLocOutOfBounds(px01, py01, sg.getWorld(p.worldId)))
				if (ServerTile.getTile(getPlayersWorld(p).getTileInWorld(px01, py01, 0)).isSolid())
					isValid = moveToLastValidLocation(p);

			if (!isTileLocOutOfBounds(px10, py10, sg.getWorld(p.worldId)))
				if (ServerTile.getTile(getPlayersWorld(p).getTileInWorld(px10, py10, 0)).isSolid())
					isValid = moveToLastValidLocation(p);

			if (!isTileLocOutOfBounds(px11, py11, sg.getWorld(p.worldId)))
				if (ServerTile.getTile(getPlayersWorld(p).getTileInWorld(px11, py11, 0)).isSolid())
					isValid = moveToLastValidLocation(p);
			
			if (isValid)
				p.lastValidLocation = p.getNewLocation();
		}

		updateTime = System.currentTimeMillis() - tickStart;
	}
	
	SGArray<Integer> toBeRemoved = new SGArray<Integer>();
	
	private static final int REVEAL_RANGE = 5;
	private static final int DELETE_RANGE = 11;

	private void tickChunks(PlayerMP p, int cx, int cy)
	{
		if (cx != p.lastChunkX || cy != p.lastChunkY)
		{
			p.lastChunkX = cx;
			p.lastChunkY = cy;

			for (int i = 0; i < REVEAL_RANGE; i++)
			{
				for (int j = 0; j < REVEAL_RANGE; j++)
				{
					int x = Util.getNumberBetween(0, World.worldWidth, cx + i - 1);
					int y = Util.getNumberBetween(0, World.worldHeight, cy + j - 1);

					if (x >= 0 && y >= 0 && x < World.worldWidth && y < World.worldHeight)
					{
						byte b = p.visitedChunks[x + y * World.worldWidth];
						if (b == 0)
						{
							p.visitedChunks[x + y * World.worldWidth] = lagChunkDelay;
							p.tickVCH.addObject(x + y * World.worldWidth);
							sendPacket(new SSetChunk(sg.world0.getChunk(x, y).getMap().getObject(0), x, y), p);
						}
					}
				}
			}
			
			int minx = Util.getNumberBetween(0, World.worldWidth, cx - DELETE_RANGE / 2);
			int miny = Util.getNumberBetween(0, World.worldHeight, cy - DELETE_RANGE / 2);
			
			int maxx = Util.getNumberBetween(0, World.worldWidth, cx + DELETE_RANGE / 2);
			int maxy = Util.getNumberBetween(0, World.worldHeight, cy + DELETE_RANGE / 2);

			for (int i = 0; i < World.worldWidth; i++)
			{
				for (int j = 0; j < World.worldHeight; j++)
				{
					if (!Util.isInRectangle(minx, miny, maxx, maxy, i, j))
					{
						if (p.visitedChunks[i + j * World.worldWidth] == -1)
						{
							sendPacket(new SSetChunk(null, i, j), p);
							p.visitedChunks[i + j * World.worldWidth] = 0;
						}
					}
				}
			}
		}
	}

	private void tickRemove(PlayerMP p)
	{
		for (int i = 0; i < p.tickVCH.getSize(); i++)
		{
			int index = p.tickVCH.getObject(i);
			byte b = p.visitedChunks[index];

			if (b > 0 && b <= lagChunkDelay)
				p.visitedChunks[index]--;

			if (p.visitedChunks[index] == 0 || p.visitedChunks[index] == -1)
				toBeRemoved.addObject(i);
		}

		toBeRemoved.reverseArray();

		for (int i : toBeRemoved)
		{
			int index = p.tickVCH.getObject(i);
			byte b = p.visitedChunks[index];
			if (index >= 0 && index < World.worldWidth * World.worldHeight && b == 0)
			{
				p.visitedChunks[index] = lagChunkDelay;
				p.tickVCH.addObject(index);
				sendPacket(new SSetChunk(sg.world0.getChunk(index % World.worldWidth, index / World.worldHeight).getMap().getObject(0),
						index % World.worldWidth, index / World.worldHeight), p);
			}
			p.tickVCH.remove(i);
		}

		toBeRemoved.clear();
	}

	private GameWorld getPlayersWorld(PlayerMP player)
	{
		return sg.world0;
	}

	private boolean moveToLastValidLocation(PlayerMP player)
	{
		sendPacket(new STeleportPlayer(player.lastValidLocation.getIntX(), player.lastValidLocation.getIntY(), player.getNetworkId()), player);

		return false;
	}

	public boolean isTileLocOutOfBounds(int x, int y, GameWorld world)
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
		newPlayer.worldId = 0;
		newPlayer.visitedChunks = new byte[World.worldWidth * World.worldHeight];
		sendPacket(new SAddEvent(new BuildStructure()), packet);
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
		PlayerMP player = new PlayerMP(ip, port, x, y);
		player.updateBox();
		sg.players.add(player);
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
	
	public void sendPacket(Packet<? extends IPacketHandler> packet, PlayerMP player)
	{
		sendPacket(packet, player.getIp(), player.getPort());
	}
	
	public List<PlayerMP> getPlayers()
	{
		return sg.players;
	}
}

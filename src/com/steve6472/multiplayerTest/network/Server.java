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

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.ServerGui;
import com.steve6472.multiplayerTest.Tile;
import com.steve6472.multiplayerTest.World;
import com.steve6472.multiplayerTest.network.handlers.ServerHandler;
import com.steve6472.multiplayerTest.network.packets.server.SAddEvent;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.world.SAddWorld;
import com.steve6472.multiplayerTest.network.packets.server.world.SSetWorld;
import com.steve6472.multiplayerTest.server.events.BuildStructure;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.networking.UDPServer;

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

			if (sg.world0.getTile(bulletTileX, bulletTileY).isSolid())
			{
				sendPacket(new SDeleteBullet(b.getNetworkId()));
				if (Util.decide(8))
				{
					int id = sg.world0.getTile(bulletTileX, bulletTileY).getId();
					if (id == Tile.wall.getId() || id == Tile.crackedWall0.getId() || id == Tile.crackedWall1.getId()
							|| id == Tile.crackedWall2.getId() || id == Tile.crackedWall3.getId() || id == Tile.crackedWall4.getId())
					{
						sg.world0.setTile(Tile.wall1.getId(), bulletTileX, bulletTileY, true);
						sendPacket(new SSpawnParticle(bulletTileX * 32 + 16, bulletTileY * 32 + 16, id, 32, 0));
					}
					if (id == Tile.wall1.getId())
					{
						sg.world0.setTile(Tile.destroyedWall.getId(), bulletTileX, bulletTileY, true);
						sendPacket(new SSpawnParticle(bulletTileX * 32 + 16, bulletTileY * 32 + 16, id, 32, 0));
					}
				} else
				{
					sendPacket(new SSpawnParticle(b.getLocation().getIntX(), b.getLocation().getIntY(),
							sg.world0.getTile(bulletTileX, bulletTileY).getId(), 4, sg.world0));
				}
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
				// System.out.println(text + " has disconnected " + remove);
			}
		}

		for (PlayerMP p : sg.players)
		{
			if (!p.checkLocation)
				continue;

			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();

			int px00 = (px + 1) / 32;
			int py00 = (py + 1) / 32;

			int px10 = (px + 32 - 1) / 32;
			int py10 = (py + 1) / 32;

			int px01 = (px + 1) / 32;
			int py01 = (py + 32 - 1) / 32;

			int px11 = (px + 32 - 1) / 32;
			int py11 = (py + 32 - 1) / 32;

			boolean isValid = true;

			if (!isTileLocOutOfBounds(px00, py00, sg.getWorld(p.worldId)))
				if (getPlayersWorld(p).getTile(px00, py00).isSolid())
					isValid = moveToLastValidLocation(p);

			if (!isTileLocOutOfBounds(px01, py01, sg.getWorld(p.worldId)))
				if (getPlayersWorld(p).getTile(px01, py01).isSolid())
					isValid = moveToLastValidLocation(p);

			if (!isTileLocOutOfBounds(px10, py10, sg.getWorld(p.worldId)))
				if (getPlayersWorld(p).getTile(px10, py10).isSolid())
					isValid = moveToLastValidLocation(p);

			if (!isTileLocOutOfBounds(px11, py11, sg.getWorld(p.worldId)))
				if (getPlayersWorld(p).getTile(px11, py11).isSolid())
					isValid = moveToLastValidLocation(p);

			if (isValid)
			{
				p.lastValidLocation = p.getNewLocation();
				/*
				 * if (px <= -16) { sendPacket(new SSetWorld(sg.world1),
				 * p.getIp(), p.getPort()); p.worldId = 1;
				 * p.setLocation(sg.getMainApp().getWidth() - 20,
				 * p.getLocation().getIntY()); sendPacket(new
				 * STeleportPlayer(p.getLocation().getIntX(),
				 * p.getLocation().getIntY(), p.getNetworkId()), p.getIp(),
				 * p.getPort()); } if (px >= sg.getMainApp().getWidth() - 16) {
				 * sendPacket(new SSetWorld(sg.world0), p.getIp(), p.getPort());
				 * p.worldId = 0; p.setLocation(-10, p.getLocation().getIntY());
				 * sendPacket(new STeleportPlayer(p.getLocation().getIntX(),
				 * p.getLocation().getIntY(), p.getNetworkId()), p.getIp(),
				 * p.getPort()); }
				 */
			}
		}

		updateTime = System.currentTimeMillis() - tickStart;
	}

	private World getPlayersWorld(PlayerMP player)
	{
		switch (player.worldId)
		{
		default:
			return sg.world0;
		case 0:
			return sg.world0;
		case 1:
			return sg.world1;
		}
	}

	private boolean moveToLastValidLocation(PlayerMP player)
	{
		sendPacket(new STeleportPlayer(player.lastValidLocation.getIntX(), player.lastValidLocation.getIntY(), player.getNetworkId()), player.getIp(),
				player.getPort());

		return false;
	}

	public boolean isTileLocOutOfBounds(int x, int y, World world)
	{
		return (x < 0 || y < 0 || x >= world.getTilesX() || y >= world.getTilesY());
	}
	
	@Override
	public void clientConnectEvent(DatagramPacket packet)
	{
		sg.playerList.addItem(packet.getAddress() + ":" + packet.getPort());

		for (PlayerMP p : sg.players)
		{
			sendPacket(new SConnectPlayer(p), packet.getAddress(), packet.getPort());
		}

		PlayerMP newPlayer = addNewPlayer(packet.getAddress(), packet.getPort(), 200, 200);
		newPlayer.worldId = 0;
		sendPacket(new SAddWorld(sg.world0), packet);
		// sendPacket(new SAddWorld(sg.world1), packet);
		sendPacket(new SSetWorld(sg.world0), packet);
		sendPacket(new SAddEvent(new BuildStructure()), packet);
		sendPacket(new SSetNetworkId(newPlayer.getNetworkId()), packet);
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
}

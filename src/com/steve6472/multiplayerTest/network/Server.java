/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network;

import java.net.DatagramPacket;
import java.net.InetAddress;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.ServerGui;
import com.steve6472.multiplayerTest.World;
import com.steve6472.multiplayerTest.network.handlers.ServerHandler;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSetWorld;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.sge.main.game.IObjectManipulator;
import com.steve6472.sge.main.networking.UDPServer;

public class Server extends UDPServer
{
	
	ServerGui sg;
	int moveLimit = 25;
	
	public static int nextNetworkId;

	public IObjectManipulator<Bullet> bullets;

	public Server(int port, ServerGui sg)
	{
		super(port);
		setIPacketHandler(new ServerHandler(this, sg));
		this.sg = sg;
		bullets = new IObjectManipulator<Bullet>();
	}

	public void tick()
	{
		bullets.tick(true);
		for (Bullet b : bullets.getAll())
		{
			for (PlayerMP p : sg.players)
			{
				if (b.getShooterNetworkId() == p.getNetworkId())
					continue;
				
//				System.out.println(b.getBox() + " " + p.getBox());
//				System.out.println(b.getShooterNetworkId() + " " + p.getNetworkId());
				
				if (b.getBox().intersects(p.getBox()))
				{
//					System.out.println("Deleting");
					p.score = 0;
					sendPacket(new SSetScore(0, p.getNetworkId()));
					
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
				b.setDead();
			}
		}
		
		for (PlayerMP p : sg.players)
		{
			if (!p.checkLocation)
				continue;
			
			int px = p.getLocation().getIntX();
			int py = p.getLocation().getIntY();

			int px00 = px / 32;
			int py00 = py / 32;
			
			int px10 = (px + 32) / 32;
			int py10 = py / 32;

			int px01 = px / 32;
			int py01 = (py + 32) / 32;

			int px11 = (px + 32) / 32;
			int py11 = (py + 32) / 32;
			
			boolean isValid = true;
			
			if (!isTileLocOutOfBounds(px00, py00, sg.world0))
				if (sg.world0.getTile(px00, py00).isSolid())
					isValid = moveToLastValidLocation(p);
			
			if (!isTileLocOutOfBounds(px01, py01, sg.world0))
				if (sg.world0.getTile(px01, py01).isSolid())
					isValid = moveToLastValidLocation(p);
			
			if (!isTileLocOutOfBounds(px10, py10, sg.world0))
				if (sg.world0.getTile(px10, py10).isSolid())
					isValid = moveToLastValidLocation(p);
			
			if (!isTileLocOutOfBounds(px11, py11, sg.world0))
				if (sg.world0.getTile(px11, py11).isSolid())
					isValid = moveToLastValidLocation(p);
			
			if (isValid)
			{
				p.lastValidLocation = p.getNewLocation();
			}
		}
	}
	
	private boolean moveToLastValidLocation(PlayerMP player)
	{
		sendPacket(new STeleportPlayer(player.lastValidLocation.getIntX(), player.lastValidLocation.getIntY(), player.getNetworkId()), player.getIp(), player.getPort());
		
		return false;
	}
	
	public boolean isTileLocOutOfBounds(int x, int y, World world)
	{
		return (x < 0 || y < 0 || x >= sg.world0.getTilesX() || y >= sg.world0.getTilesY());
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
		sendPacket(new SSetWorld(sg.world0), packet);
		sendPacket(new SSetNetworkId(newPlayer.getNetworkId()), packet);
		
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

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network;

import java.net.DatagramPacket;
import java.util.Iterator;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.handlers.ClientHandler;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.UDPClient;
import com.steve6472.sge.main.networking.packet.IPacketHandler;
import com.steve6472.sge.main.networking.packet.Packet;

public class Client extends UDPClient
{
	ClientGui cg;
	public int networkId = -1;
	
	ClientHandler ch;

	public Client(String ip, int port, ClientGui cg)
	{
		super(ip, port);
		ch = new ClientHandler(this, cg);
		setIPacketHandler(ch);
		this.cg = cg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handlePacket(Packet<?> packet, int packetId, DatagramPacket p)
	{
		if (packet instanceof SPacket)
		{
			SPacket sPacket = (SPacket) packet;
			sPacket.handlePacket(this, cg);
		} else
		{
			((Packet<IPacketHandler>)packet).handlePacket(packetHandler);
		}
	}
	
	public Bullet getBullet(int networkId)
	{
		for (Iterator<Bullet> iter = cg.bullets.getAll().iterator(); iter.hasNext();)
		{
			Bullet b = iter.next();
			if (b.getNetworkId() == networkId)
				return b;
		}
		
		return null;
	}
	
	public int getBulletIndex(int networkId)
	{
		int i = 0;
		for (Iterator<Bullet> iter = cg.bullets.getAll().iterator(); iter.hasNext();)
		{
			Bullet b = iter.next();
			if (b.getNetworkId() == networkId)
				return i;
			i++;
		}
		
		return -1;
	}

	public PlayerMP getPlayer(int networkId)
	{
		for (PlayerMP p : cg.players)
		{
			if (p.getNetworkId() == networkId)
			{
				return p;
			}
		}
		return null;
	}
	
	public GameWorld getWorld()
	{
		return cg.world;
	}

}

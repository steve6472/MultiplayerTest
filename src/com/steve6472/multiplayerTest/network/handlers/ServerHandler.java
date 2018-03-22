/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CLeftPress;
import com.steve6472.multiplayerTest.network.packets.client.CLeftRelease;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SChangeTile;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.sge.main.Util;

public class ServerHandler implements IServerHandler
{
	private final Server server;
	private final ServerGui serverGui;

	public ServerHandler(Server server, ServerGui serverGui)
	{
		this.server = server;
		this.serverGui = serverGui;
	}

	@Override
	public void handleMovePacket(CMovePacket packet)
	{
		int x = packet.x;
		int y = packet.y;
		PlayerMP player = server.getPlayer(packet.getSender());
		player.checkLocation();
		player.setLocation(x, y);
		player.updateBox();
		server.sendPacketWithException(new STeleportPlayer(x, y, player.getNetworkId()), packet.getSender());
	}

	@Override
	public void handleLeftPressPacket(CLeftPress packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		int px = player.lastValidLocation.getIntX() + 16;
		int py = player.lastValidLocation.getIntY() + 16;
		server.sendPacket(new SSpawnParticle(px, py, 0, 16));
		SSpawnBullet bulletPacket = new SSpawnBullet(px, py, Util.countAngle(packet.getX(), packet.getY(), px, py), Server.nextNetworkId++,
				player.getNetworkId());
		server.bullets.add(bulletPacket.createBullet());
		server.sendPacket(bulletPacket);
	}

	@Override
	public void handleLeftReleasePacket(CLeftRelease packet)
	{
	}
	
	@Override
	public void handleRequestTile(CRequestTile packet)
	{
//		System.out.println("Client requested tile " + packet.getIndex());
		server.sendPacket(new SChangeTile(packet.getIndex(), serverGui.world0.getTileId(packet.getIndex())), packet.getSender());
	}
	
	@Override
	public void handleSetName(CSetName packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player != null)
		{
			player.setName(packet.getName());
			server.sendPacketWithException(new SSetName(packet.getName(), player.getNetworkId()), packet.getSender());
		} else
		{
			System.err.println("Can't find player from datagram: " + packet.getSender().getAddress().getHostAddress() + ":" + packet.getSender().getPort());
		}
	}
	
	@Override
	public void handleChat(CChat packet)
	{
		server.sendPacket(new SChat(packet.getText(), server.getPlayer(packet.getSender()).getNetworkId()));
	}

}

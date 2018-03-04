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
import com.steve6472.multiplayerTest.network.packets.client.CLeftPress;
import com.steve6472.multiplayerTest.network.packets.client.CLeftRelease;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SChangeTile;
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
		player.setLocation(x, y);
		player.updateBox();
		server.sendPacketWithException(new STeleportPlayer(x, y, player.getNetworkId()), packet.getSender());
	}

	@Override
	public void handleLeftPressPacket(CLeftPress packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		server.sendPacket(new SSpawnParticle(player.getLocation().getIntX() + 16, player.getLocation().getIntY() + 16, 0, 16));
		SSpawnBullet bulletPacket = new SSpawnBullet(player.getLocation().getIntX() + 16, player.getLocation().getIntY() + 16,
				Util.countAngle(packet.getX(), packet.getY(), player.getLocation().getX() + 16, player.getLocation().getY() + 16), Server.nextNetworkId++, player.getNetworkId());
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

}

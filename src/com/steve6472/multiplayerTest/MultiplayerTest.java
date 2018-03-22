/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import static org.lwjgl.glfw.GLFW.*;

import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CLeftPress;
import com.steve6472.multiplayerTest.network.packets.client.CLeftRelease;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.server.SChangeTile;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSetWorld;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.EntityList;
import com.steve6472.sge.main.networking.packet.DisconnectPacket;
import com.steve6472.sge.main.networking.packet.Packet;

public class MultiplayerTest extends MainApplication
{
	public ServerGui serverGui;
	public ClientGui clientGui;
	
	public static EntityList entityList;

	@Override
	public void init()
	{
		entityList = new EntityList(this);
		
		entityList.addEntity(Bullet.class);
		
		Packet.addPacket(2, 	SConnectPlayer.class);
		Packet.addPacket(3, 	SDisconnectPlayer.class);
		Packet.addPacket(4, 	SDeleteBullet.class);
		Packet.addPacket(5, 	SSpawnBullet.class);
		Packet.addPacket(6, 	SSpawnParticle.class);
		Packet.addPacket(7, 	STeleportPlayer.class);
		Packet.addPacket(8, 	CLeftPress.class);
		Packet.addPacket(9, 	CLeftRelease.class);
		Packet.addPacket(10, 	CMovePacket.class);
		Packet.addPacket(11, 	SSetWorld.class);
		Packet.addPacket(12, 	SChangeTile.class);
		Packet.addPacket(13, 	CRequestTile.class);
		Packet.addPacket(14, 	SSetNetworkId.class);
		Packet.addPacket(15, 	SSetScore.class);
		Packet.addPacket(16, 	SSetName.class);
		Packet.addPacket(17, 	CSetName.class);
		Packet.addPacket(18, 	SChat.class);
		Packet.addPacket(19, 	CChat.class);

		serverGui = new ServerGui(this);
		clientGui = new ClientGui(this);
		
		new MenuGui(this);
	}

	@Override
	public void tick()
	{
		tickGui();
	}

	@Override
	public void render(Screen screen)
	{
		renderGui();
	}

	public void exit()
	{
		if (clientGui.client != null)
			clientGui.client.sendPacket(new DisconnectPacket());
		glfwSetWindowShouldClose(window, true);
	}

	@Override
	public int getWidth()
	{
		return 16 * 32 * 2;
	}

	@Override
	public int getHeight()
	{
		return 9 * 32 * 2;
	}

	@Override
	public String getTitle()
	{
		return null;
	}

	@Override
	public void setWindowHints()
	{
		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
	}
	
	public static void main(String[] args)
	{
		new MultiplayerTest();
	}

}

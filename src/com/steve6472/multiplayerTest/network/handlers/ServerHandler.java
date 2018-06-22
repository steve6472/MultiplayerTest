/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import java.net.DatagramPacket;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.client.CChangeSlot;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CConfirmChunk;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CPing;
import com.steve6472.multiplayerTest.network.packets.client.CRequestInventory;
import com.steve6472.multiplayerTest.network.packets.client.CRotate;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.client.CUpdatePacket;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SChangeSlot;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SOpenInventory;
import com.steve6472.multiplayerTest.network.packets.server.SPingResponse;
import com.steve6472.multiplayerTest.network.packets.server.SRotate;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.world.World;

public class ServerHandler implements IServerHandler
{
	private final Server server;

	public ServerHandler(Server server, ServerGui serverGui)
	{
		this.server = server;
	}

	@Override
	public void handleMovePacket(CMovePacket packet)
	{
		int x = packet.x;
		int y = packet.y;
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player == null)
		{
			printCantFindPlayerErrorMessage(packet.getSender());
			return;
		}
		player.lastUpdate = System.currentTimeMillis();
		if (Util.getDistance(player.getLocation().getIntX(), player.getLocation().getIntY(), x, y) > Server.moveLimit)
		{
			server.sendPacket(new STeleportPlayer(player.lastValidLocation.getIntX(), player.lastValidLocation.getIntY(), player.getNetworkId()),
					player.getIp(), player.getPort());
			return;
		}
		player.checkLocation();
		player.setLocation(x, y);
		player.updateBox();
		server.sendPacketWithException(new STeleportPlayer(x, y, player.getNetworkId()), packet.getSender());
	}
	
	@Override
	public void handleSetName(CSetName packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player != null)
		{
			player.setName(packet.getName());
			player.lastUpdate = System.currentTimeMillis();
			server.sendPacketWithException(new SSetName(packet.getName(), player.getNetworkId()), packet.getSender());
		} else
		{
			printCantFindPlayerErrorMessage(packet.getSender());
		}
	}
	
	@Override
	public void handleChat(CChat packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player != null)
		{
			player.lastUpdate = System.currentTimeMillis();
		} else
		{
			printCantFindPlayerErrorMessage(packet.getSender());
		}
		server.sendPacket(new SChat(packet.getText(), server.getPlayer(packet.getSender()).getNetworkId()));
	}
	
	@Override
	public void handleUpdate(CUpdatePacket packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player != null)
		{
			player.lastUpdate = System.currentTimeMillis();
		} else
		{
			printCantFindPlayerErrorMessage(packet.getSender());
		}
	}
	
	@Override
	public void handlePing(CPing packet)
	{
		server.sendPacket(new SPingResponse(), packet.getSender());
	}
	
	@Override
	public void handleRotation(CRotate packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player == null)
			return;
		player.setAngle(packet.getDegree());
		server.sendPacketWithException(new SRotate(player.getNetworkId(), packet.getDegree()), packet.getSender());
	}
	
	@Override
	public void handleChunkConfirm(CConfirmChunk packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player != null)
		{
			player.visitedChunks[packet.getChunkX() + packet.getChunkY() * World.worldWidth] = -1;
		} else
		{
			printCantFindPlayerErrorMessage(packet.getSender());
		}
	}
	
	@Override
	public void handleSlotChange(CChangeSlot packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player != null)
		{
			player.slot = packet.getSlot();
			server.sendPacketWithException(new SChangeSlot(packet.getSlot(), player.getNetworkId()), packet.getSender());
		} else
		{
			printCantFindPlayerErrorMessage(packet.getSender());
		}
	}
	
	@Override
	public void handleInventoryRequest(CRequestInventory packet)
	{
		PlayerMP player = server.getPlayer(packet.getSender());
		if (player != null)
		{
			player.sendPacket(new SOpenInventory(player.createClientInventory(), 8, 4));
		} else
		{
			printCantFindPlayerErrorMessage(packet.getSender());
		}
	}
	
	public static void printCantFindPlayerErrorMessage(DatagramPacket datagram)
	{
		System.err.println("Can't find player from datagram: " + datagram.getAddress().getHostAddress() + ":" + datagram.getPort());
	}

}

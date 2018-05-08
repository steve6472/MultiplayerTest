/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.handlers;

import java.net.DatagramPacket;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.client.CChangeSlot;
import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CConfirmChunk;
import com.steve6472.multiplayerTest.network.packets.client.CMouseButton;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CPing;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.client.CRotate;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.client.CUpdatePacket;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.multiplayerTest.network.packets.server.SChangeSlot;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SPingResponse;
import com.steve6472.multiplayerTest.network.packets.server.SRotate;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.world.World;

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
	public void handleMouseButtonPacket(CMouseButton packet)
	{
		{
			PlayerMP player = server.getPlayer(packet.getSender());
			{
				if (player != null)
				{
					int tx = (packet.getX() - serverGui.getMainApp().getWidth() / 2 + player.getX()) / 32;
					int ty = (packet.getY() - serverGui.getMainApp().getHeight() / 2 + player.getY()) / 32;

					int id = serverGui.world0.getTileInWorldSafe(tx, ty, 0);
					ServerTile.getTile(id).mouseEvent(tx, ty, player, packet.getAction(), packet.getButton(), serverGui.world0);
				}
			}
		}

		if (packet.getAction() == 0 && packet.getButton() == 1) //LMB
		{
			PlayerMP player = server.getPlayer(packet.getSender());
			player.lastUpdate = System.currentTimeMillis();
			if (player.slot == 2)
			{
				int px = player.lastValidLocation.getIntX() + 16;
				int py = player.lastValidLocation.getIntY() + 16;
				SSpawnBullet bulletPacket = new SSpawnBullet(px, py,
						Util.countAngle(packet.getX(), packet.getY(), serverGui.getMainApp().getWidth() / 2, serverGui.getMainApp().getHeight() / 2)
								+ Util.getRandomDouble(2, -2),
						Server.nextNetworkId++, player.getNetworkId());
				server.bullets.add(bulletPacket.createBullet());
				server.sendPacket(bulletPacket);
			} else if (player.slot == 1)
			{
				int tx = (packet.getX() - serverGui.getMainApp().getWidth() / 2 + player.getX()) / 32;
				int ty = (packet.getY() - serverGui.getMainApp().getHeight() / 2 + player.getY()) / 32;

				if (Util.getDistance(tx, ty, player.getTileX(), player.getTileY()) <= 4)
				{
					int currentId = serverGui.world0.getTileInWorld(tx, ty, 0);
					if (currentId == ServerTile.wallBlueprint.getId())
					{
						serverGui.world0.setTileInWorld(tx, ty, 0, ServerTile.grass.getId(), true);
					}
				}
			}
		} else if (packet.getAction() == 0 && packet.getButton() == 2) //RMB
		{
			PlayerMP player = server.getPlayer(packet.getSender());
			if (player == null)
			{
				printCantFindPlayerErrorMessage(packet.getSender());
				return;
			} else
			{
				if (player.slot == 1)
				{
					int tx = (packet.getX() - serverGui.getMainApp().getWidth() / 2 + player.getX()) / 32;
					int ty = (packet.getY() - serverGui.getMainApp().getHeight() / 2 + player.getY()) / 32;

					if (Util.getDistance(tx, ty, player.getTileX(), player.getTileY()) <= 4)
					{
						int currentId = serverGui.world0.getTileInWorld(tx, ty, 0);

						if (currentId == ServerTile.grass.getId() || currentId == ServerTile.grassWithFlowers0.getId() || currentId == ServerTile.destroyedWall.getId())
						{
							serverGui.world0.setTileInWorld(tx, ty, 0, ServerTile.wallBlueprint.getId(), true);
						}
//						else if (currentId == Tile.wallBlueprint.getId())
//						{
//							serverGui.world0.setTileInWorld(tx, ty, 0, Tile.wallProg0.getId(), true);
//						} else if (Util.isNumberInRange(Tile.wallProg0.getId(), Tile.wallProg13.getId(), currentId))
//						{
//							serverGui.world0.setTileInWorld(tx, ty, 0, currentId + 1, true);
//						} else if (currentId == Tile.wallProg14.getId())
//						{
//							serverGui.world0.setTileInWorld(tx, ty, 0, Tile.wall.getId(), true);
//						}
					}
				}
			}
		}
	}

	@Override
	public void handleRequestTile(CRequestTile packet)
	{
		System.out.println("Client requested tile " + packet.getIndex());
		System.err.println("Can not send tile due to client not providing world id");
//		server.sendPacket(new SChangeTile(packet.getIndex(), serverGui.world0.getTileId(packet.getIndex())), packet.getSender());
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
	
	private void printCantFindPlayerErrorMessage(DatagramPacket datagram)
	{
		System.err.println("Can't find player from datagram: " + datagram.getAddress().getHostAddress() + ":" + datagram.getPort());
	}

}

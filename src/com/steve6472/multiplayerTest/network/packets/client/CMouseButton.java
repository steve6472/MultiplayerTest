/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import java.net.DatagramPacket;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.CPacket;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.networking.packet.DataStream;

public class CMouseButton extends CPacket
{

	/**
	 * pressed position on screen
	 */
	int x, y;
	/**
	 * Pressed tile coord
	 */
	int tx, ty;
	/**
	 * screen width & height
	 */
	int sw, sh;
	int button;
	/**
	 * 0 - Press
	 * 1 - Release
	 */
	int action;
	
	public CMouseButton()
	{
	}
	
	public CMouseButton(int x, int y, int tx, int ty, int sw, int sh, int button, int action)
	{
		this.x = x;
		this.y = y;
		
		this.tx = tx;
		this.ty = ty;
		
		this.sw = sw;
		this.sh = sh;
		
		this.button = button;
		this.action = action;
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
		
		output.writeInt(tx);
		output.writeInt(ty);
		
		output.writeInt(sw);
		output.writeInt(sh);
		
		output.writeInt(button);
		output.writeInt(action);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
		
		this.tx = input.readInt();
		this.ty = input.readInt();
		
		this.sw = input.readInt();
		this.sh = input.readInt();
		
		this.button = input.readInt();
		this.action = input.readInt();
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getAction()
	{
		return action;
	}
	
	public int getButton()
	{
		return button;
	}

	@Override
	public void handlePacket(Server server, ServerGui serverGui)
	{
		{
			PlayerMP player = server.getPlayer(getSender());
			{
				if (player != null)
				{
					int id = serverGui.world0.getTileInWorldSafe(tx, ty, 0);
					ServerTile.getTile(id).mouseEvent(tx, ty, player, getAction(), getButton(), serverGui.world0);
				}
			}
		}

		if (getAction() == 0 && getButton() == 1) //LMB
		{
			PlayerMP player = server.getPlayer(getSender());
			player.lastUpdate = System.currentTimeMillis();
			if (player.slot == 2)
			{
				int px = player.lastValidLocation.getIntX() + 16;
				int py = player.lastValidLocation.getIntY() + 16;
				SSpawnBullet bulletPacket = new SSpawnBullet(px, py,
						Util.countAngle(getX(), getY(), sw / 2, sh / 2)
								+ Util.getRandomDouble(2, -2),
						Server.nextNetworkId++, player.getNetworkId());
				server.bullets.add(bulletPacket.createBullet());
				server.sendPacket(bulletPacket);
			} else if (player.slot == 1)
			{
				if (Util.getDistance(tx, ty, player.getTileX(), player.getTileY()) <= 4)
				{
					int currentId = serverGui.world0.getTileInWorld(tx, ty, 0);
					if (currentId == ServerTile.wallBlueprint.getId())
					{
						serverGui.world0.setTileInWorld(tx, ty, 0, ServerTile.grass.getId(), true);
					}
				}
			}
		} else if (getAction() == 0 && getButton() == 2) //RMB
		{
			PlayerMP player = server.getPlayer(getSender());
			if (player == null)
			{
				printCantFindPlayerErrorMessage(getSender());
				return;
			} else
			{
				if (player.slot == 1)
				{
					if (Util.getDistance(tx, ty, player.getTileX(), player.getTileY()) <= 4)
					{
						int currentId = serverGui.world0.getTileInWorldSafe(tx, ty, 0);

						if (currentId == ServerTile.grass.getId() || currentId == ServerTile.grassWithFlowers0.getId() || currentId == ServerTile.destroyedWall.getId())
						{
							serverGui.world0.setTileInWorld(tx, ty, 0, ServerTile.wallBlueprint.getId(), true);
						}
					}
				}
			}
		}
	}
	
	private void printCantFindPlayerErrorMessage(DatagramPacket datagram)
	{
		System.err.println("Can't find player from datagram: " + datagram.getAddress().getHostAddress() + ":" + datagram.getPort());
	}

}

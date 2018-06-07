/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 6. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client.inv;

import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.CPacket;
import com.steve6472.multiplayerTest.server.tiles.BaseTile;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.multiplayerTest.server.tiles.tileData.TileData;
import com.steve6472.sge.main.networking.packet.DataStream;

public class CMoveItem extends CPacket
{
	int indexFrom, indexTo;
	
	/**
	 * 0 = player inventory
	 * 1 = other inventory
	 */
	int inventoryFrom, inventoryTo;
	

	public CMoveItem(int indexFrom, int indexTo, int inventoryFrom, int inventoryTo)
	{
		this.indexFrom = indexFrom;
		this.indexTo = indexTo;
		this.inventoryFrom = inventoryFrom;
		this.inventoryTo = inventoryTo;
	}
	
	public CMoveItem()
	{
	}

	@Override
	public void handlePacket(Server server, ServerGui serverGui)
	{
		GameWorld world = serverGui.world0;
		PlayerMP player = server.getPlayer(getSender());
		ServerTile tile = ServerTile.getTile(world.getTileInWorld(player.inventoryTileX, player.inventoryTileY, player.inventoryTileL));
		
		if (tile instanceof BaseTile)
			if (!((BaseTile) tile).hasTileData())
				if (tile != ServerTile.chest)
					return;
		
		TileData data = world.getTileData(player.inventoryTileX, player.inventoryTileY, player.inventoryTileL);
		
//		if (inventoryFrom == 1 && inventoryTo == 1)
		{
			for (Object o : data)
				System.out.print(o + " ");
			System.out.println();
			
			data.swap(indexFrom + 2, indexTo + 2);
			
			for (Object o : data)
				System.out.print(o + " ");
			System.out.println();
			
			System.out.println("\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/\\/");
		}
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(indexFrom);
		output.writeInt(indexTo);
		output.writeInt(inventoryFrom);
		output.writeInt(inventoryTo);
	}

	@Override
	public void input(DataStream input)
	{
		indexFrom = input.readInt();
		indexTo = input.readInt();
		inventoryFrom = input.readInt();
		inventoryTo = input.readInt();
	}
}

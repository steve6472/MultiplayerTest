/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles;

import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.network.packets.server.SOpenInventory;
import com.steve6472.multiplayerTest.server.tiles.tileData.ChestController;
import com.steve6472.multiplayerTest.server.tiles.tileData.TileData;

public class Chest extends BaseTile
{
	public Chest(int id, String sprite)
	{
		super(id, sprite, true, false, 0, 0xffb56418);
		createHitParticles();
		setTileDataController(new ChestController());
	}

	@Override
	public void mouseEvent(int tx, int ty, PlayerMP player, int action, int button, GameWorld world)
	{
		if (action == 0 && button == 2)
		{
			TileData td = world.getTileData(tx, ty, 0);
			int x = td.getInt(0);
			int y = td.getInt(1);
			int[] ids = new int[x * y];
			for (int i = 0; i < x * y; i++)
			{
				ids[i] = td.getInt(2 + i);
			}
			player.sendPacket(new SOpenInventory(ids, 5, 5));
			player.inventory = 1;
			player.inventoryTileX = tx;
			player.inventoryTileY = ty;
			player.inventoryTileL = 0;
		}
	}
	
}

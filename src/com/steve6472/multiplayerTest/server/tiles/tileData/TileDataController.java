/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles.tileData;

import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.World;

public abstract class TileDataController
{

	public TileDataController()
	{
	}
	
	public abstract void tick(TileData data, int id, int x, int y, int l, Chunk chunk, World world);
	
	public abstract TileData generateTileData();

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles.tileData;

import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.World;

public class ChestController extends TileDataController
{

	public ChestController()
	{
	}

	@Override
	public void tick(TileData data, int id, int x, int y, int l, Chunk chunk, World world)
	{
	}

	@Override
	public TileData generateTileData()
	{
		TileData data = new TileData(2);
		data.setInt(0, 5);
		data.setInt(1, 5);
		for (int i = 0; i < 25; i++)
		{
			data.addInt(Util.getRandomInt(5, 1));
		}
		return data;
	}

}

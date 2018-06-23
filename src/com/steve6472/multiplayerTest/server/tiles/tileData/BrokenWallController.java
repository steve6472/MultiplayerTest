/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 22. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles.tileData;

import com.steve6472.multiplayerTest.server.ServerChunk;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.main.Util;

public class BrokenWallController extends TileDataController
{

	public BrokenWallController()
	{
	}

	@Override
	public void tick(TileData data, int id, int x, int y, int l, ServerChunk chunk, ServerWorld world)
	{
		if (Util.decide(2000))
			world.setTileInWorld(x, y, l, ServerTile.grass.getId(), true);
	}

	@Override
	public TileData generateTileData()
	{
		return new TileData(0);
	}

}

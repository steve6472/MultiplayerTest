/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles.tileData;

import com.steve6472.multiplayerTest.server.ServerChunk;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;

public class ParticleBlock extends TileDataController
{

	public ParticleBlock()
	{
	}

	@Override
	public void tick(TileData data, int id, int x, int y, int l, ServerChunk chunk, ServerWorld world)
	{
		world.createTileHitParticles(x, y, x * 32 + 16, y * 32 + 16, ServerTile.rainbow.getId());
	}

	@Override
	public TileData generateTileData()
	{
		return new TileData(0);
	}

}

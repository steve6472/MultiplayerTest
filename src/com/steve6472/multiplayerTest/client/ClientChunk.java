/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.client;

import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.World;

public class ClientChunk extends Chunk
{

	public ClientChunk(World world, int chunkX, int chunkY)
	{
		super(world, chunkX, chunkY);
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

import com.steve6472.multiplayerTest.server.ServerWorld;

public abstract class Structure
{
	public abstract int[] getStructure();
	
	public abstract int getStructureWidth();
	public abstract int getStructureHeight();
	
	public void generateStructure(int x, int y, ServerWorld world)
	{
		for (int i = 0; i < getStructureWidth(); i++)
		{
			for (int j = 0; j < getStructureHeight(); j++)
			{
				setTile(x + i, y + j, getStructure()[i + j * getStructureWidth()], world);
			}
		}
	}
	
	private void setTile(int x, int y, int id, ServerWorld world)
	{
//		if (x < 0 || y < 0 || x >= World.worldWidth * Chunk.chunkWidth || y >= World.worldHeight * Chunk.chunkHeight)
//			return;
		world.setTileInWorld(x, y, 0, id, false);
//		world.setTile(id, x, y, false);
	}

}

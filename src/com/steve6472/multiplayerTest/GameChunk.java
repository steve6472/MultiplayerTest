/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.multiplayerTest.server.tiles.BaseTile;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.multiplayerTest.server.tiles.tileData.TileData;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.game.world.Chunk;

public class GameChunk extends Chunk
{
	private SGArray<TileData[]> tileData;
	
	public GameChunk()
	{
		map = new SGArray<int[]>(layerCount, false, false);
		tileData = new SGArray<TileData[]>(layerCount, false, false);
		
		for (int l = 0; l < layerCount; l++)
		{
			map.setObject(l, new int[chunkWidth * chunkHeight]);
			tileData.setObject(l, new TileData[chunkWidth * chunkHeight]);
		}
	}
	
	@Override
	public boolean setTileIdSafe(int x, int y, int layer, int id)
	{
		if (super.setTileIdSafe(x, y, layer, id))
		{
			ServerTile tile = ServerTile.getTile(id);
			if (tile instanceof BaseTile)
			{
				if (id == 30)
				System.out.println("Setting " + id + " at " + x + "/" + y + " l:" + layer);
				if (((BaseTile) tile).hasTileData())
				{
					setTileData(x, y, layer, ((BaseTile) tile).getTileDataController().generateTileData());
					System.out.println("Created Tile Data");
				}
			} else
			{
				setTileData(x, y, layer, null);
			}
			return true;
		} else
		{
			return false;
		}
	}
	
	public TileData getTileDataSafe(int x, int y, int layer)
	{
		return isCoordInBounds(x, y, layer) ? tileData.getObject(layer)[x + y * chunkWidth] : null;
	}
	
	public TileData getTileData(int x, int y, int layer)
	{
		return tileData.getObject(layer)[x + y * chunkWidth];
	}
	
	public boolean setTileDataSafe(int x, int y, int layer, TileData data)
	{
		boolean flag = isCoordInBounds(x, y, layer);
		if (flag)
			tileData.getObject(layer)[x + y * chunkWidth] = data;
		return flag;
	}

	public void setTileData(int x, int y, int layer, TileData data)
	{
		tileData.getObject(layer)[x + y * chunkWidth] = data;
	}
}

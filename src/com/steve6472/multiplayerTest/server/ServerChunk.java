/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server;

import com.steve6472.multiplayerTest.server.tiles.BaseTile;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.multiplayerTest.server.tiles.tileData.TileData;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.World;

public class ServerChunk extends Chunk
{
	private SGArray<TileData[]> tileData;
	
	public ServerChunk(World world, int chunkX, int chunkY)
	{
		super(world, chunkX, chunkY);
		tileData = new SGArray<TileData[]>(layerCount);
		
		for (int l = 0; l < layerCount; l++)
		{
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
				if (id == 30 || id == ServerTile.particleTest.getId() || id == ServerTile.destroyedWall.getId())
					System.out.println("Setting " + id + " at " + x + "/" + y + " l:" + layer);
				
				if (((BaseTile) tile).hasTileData())
				{
					setTileData(x, y, layer, ((BaseTile) tile).getTileDataController().generateTileData());
					System.out.println("Created Tile Data for " + tile.getClass().getName() + " at " + x + "/" + y);
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
		int X = x + chunkWidth * chunkX;
		int Y = y + chunkHeight * chunkY;
		System.out.println("New Tile Data at " + X + "/" + Y + "/ L:" + layer + " data: " + data);
		if (data == null)
		{
			ServerWorld gw = (ServerWorld) world;
			gw.tileDataLocations.remove(Integer.valueOf(X + Y * (World.worldWidth * Chunk.chunkWidth)));
		} else
		{
			ServerWorld gw = (ServerWorld) world;
			gw.tileDataLocations.add(Integer.valueOf(X + Y * (World.worldWidth * Chunk.chunkWidth)));
			gw.tileDataLocations.printContent();
		}
		tileData.get(layer)[x + y * chunkWidth] = data;
	}
}

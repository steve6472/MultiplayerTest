/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.main.game.IObject;

public class World implements IObject
{
	private final int width;
	private final int height;
	private final int worldId;
	
	private final int[] tiles;
	
	public int need = -1;

	public World(int width, int height, int worldId)
	{
		this.width = width;
		this.height = height;
		this.worldId = worldId;
		this.tiles = new int[width * height];
	}

	@Override
	public void render(Screen screen)
	{
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				screen.drawSprite(i * 32, j * 32, Tile.getTile(tiles[i + j * width]).getSprite());
			}
		}
	}

	@Override
	public void tick()
	{
		need = -1;
		
		for (int i = 0; i < width * height; i++)
		{
			if (tiles[i] == 0)
			{
				need = i;
				break;
			}
		}
	}
	
	public void setTile(int id, int x, int y)
	{
		tiles[x + y * width] = id;
	}
	
	public void setTile(int id, int index)
	{
		tiles[index] = id;
	}
	
	public int getTileId(int x, int y)
	{
		return tiles[x + y * width];
	}
	
	public int getTileId(int index)
	{
		return tiles[index];
	}
	
	public Tile getTile(int x, int y)
	{
		return Tile.getTile(getTileId(x, y));
	}
	
	public int getTilesX()
	{
		return width;
	}
	
	public int getTilesY()
	{
		return height;
	}
	
	public int getWorldId()
	{
		return worldId;
	}

}

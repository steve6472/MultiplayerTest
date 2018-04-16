/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.util.ArrayList;
import java.util.List;

import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.Atlas;

public class Tile
{
	public static final Tile air = new Tile(0, "", true, false, 0);
	public static final Tile crackedWall0 = new Tile(1, "crackedWall0.png", true, true, 0);
	public static final Tile crackedWall1 = new Tile(2, "crackedWall1.png", true, true, 0);
	public static final Tile crackedWall2 = new Tile(3, "crackedWall2.png", true, true, 0);
	public static final Tile crackedWall3 = new Tile(4, "crackedWall3.png", true, true, 0);
	public static final Tile crackedWall4 = new Tile(5, "crackedWall4.png", true, true, 0);
	public static final Tile grass = new Tile(6, "grass.png", false, false, 0);
	public static final Tile grassWithFlowers0 = new Tile(7, "grassWithFlowers0.png", false, false, 0);
	public static final Tile sand = new Tile(8, "sand.png", false, false, 0);
	public static final Tile wall = new Tile(9, "wall.png", true, true, 0);
	public static final Tile water = new Tile(10, "water.png", true, false, 0);
	public static final Tile wall1 = new Tile(11, "wall1.png", true, true, 0);
	public static final Tile destroyedWall = new Tile(12, "destroyed_wall.png", false, false, 0);
	
	@SuppressWarnings("unused")
	private static final Tile NULL = new Tile(-1, null, false, false, 0);
	
	public static Atlas atlas;
	
	private static Tile[] tiles;
	private static String[] sprites;
	
	private final int id;
	private final int light;
//	private final Sprite sprite;
	private final boolean isSolid;
	private final boolean castShadow;
	
	public static int totalCount = 13;
	
	private int indexX = 0;
	private int indexY = 0;
	
	public Tile(int id, String sprite, boolean isSolid, boolean castShadow, int light)
	{
		if (id == -1 && sprite == null)
		{
			createAtlas();
			this.id = id;
			this.isSolid = isSolid;
			this.light = light;
			this.castShadow = castShadow;
			return;
		}
		
		if (id == 0)
		{
			sprites = new String[255];
			tiles = new Tile[255];
		}
		
		this.id = id;
		this.isSolid = isSolid;
		this.light = light;
		this.castShadow = castShadow;
		
		totalCount = Util.maxi(id, totalCount);
		
		sprites[id] = sprite;
		tiles[id] = this;
	}
	
	private static void createAtlas()
	{
		List<String> strs = new ArrayList<String>();
		for (String s : sprites)
		{
			if (s == null)
				break;
			strs.add(s);
		}
		atlas = new Atlas(strs);
		atlas.create(32, (x, y, i) -> tiles[i].setIndexes(x, y, i));
		for (Tile t : tiles)
		{
			if (t == null)
				break;
			System.out.println(t.getId() + " " + t.getIndexX() + " " + t.getIndexY());
		}
	}
	
	private void setIndexes(int x, int y, int index)
	{
//		System.out.println(x + "/" + y + " " + index);
		tiles[index].indexX = x;
		tiles[index].indexY = y;
	}
	
	public int getIndexX()
	{
		return indexX;
	}
	
	public int getIndexY()
	{
		return indexY;
	}
	
	public boolean isSolid()
	{
		return isSolid;
	}
	
	public boolean castShadow()
	{
		return castShadow;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getLight()
	{
		return light;
	}
	
	public static Tile getTile(int id)
	{
		return tiles[id];
	}
	
	public static Tile[] getTiles()
	{
		return tiles;
	}
	
}

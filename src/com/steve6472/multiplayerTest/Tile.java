/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.gfx.Sprite;

public class Tile
{
	public static final Tile air = new Tile(0, new Sprite(), false, 0);
	public static final Tile crackedWall0 = new Tile(1, new Sprite("crackedWall0.png"), true, 0);
	public static final Tile crackedWall1 = new Tile(2, new Sprite("crackedWall1.png"), true, 0);
	public static final Tile crackedWall2 = new Tile(3, new Sprite("crackedWall2.png"), true, 0);
	public static final Tile crackedWall3 = new Tile(4, new Sprite("crackedWall3.png"), true, 0);
	public static final Tile crackedWall4 = new Tile(5, new Sprite("crackedWall4.png"), true, 0);
	public static final Tile grass = new Tile(6, new Sprite("grass.png"), false, 0);
	public static final Tile grassWithFlowers0 = new Tile(7, new Sprite("grassWithFlowers0.png"), false, 0);
	public static final Tile sand = new Tile(8, new Sprite("sand.png"), false, 0);
	public static final Tile wall = new Tile(9, new Sprite("wall.png"), true, 0);
	public static final Tile water = new Tile(10, new Sprite("water.png"), true, 0);
	
	private static Tile[] tiles;
	
	private final int id;
	private final int light;
	private final Sprite sprite;
	private final boolean isSolid;
	
	public Tile(int id, Sprite sprite, boolean isSolid, int light)
	{
		if (id == 0)
			tiles = new Tile[255];
		
		this.id = id;
		this.sprite = sprite;
		this.isSolid = isSolid;
		this.light = light;
		tiles[id] = this;
	}
	
	public Sprite getSprite()
	{
		return sprite;
	}
	
	public boolean isSolid()
	{
		return isSolid;
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
	
}

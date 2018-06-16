/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 6. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.tiles;

import com.steve6472.multiplayerTest.Bullet;
import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.game.Atlas;
import com.steve6472.sge.main.game.world.GameTile;

public abstract class ServerTile extends GameTile
{
	private final int id;
	private final int light;
	private final int mapColor;
	private final String sprite;
	private final boolean isSolid;
	private final boolean castShadow;
	
	private int indexX = 0;
	private int indexY = 0;
	
	private static Atlas atlas;
	private static SGArray<String> sprites = new SGArray<String>();
	private static SGArray<ServerTile> tiles = new SGArray<ServerTile>();
	
	public ServerTile(int id, String sprite, boolean isSolid, boolean castShadow, int light, int mapColor)
	{
		this.id = id;
		this.isSolid = isSolid;
		this.mapColor = mapColor;
		this.light = light;
		this.castShadow = castShadow;
		this.sprite = sprite == null || sprite.isEmpty() ? null :"tiles\\" + sprite;
	}

	public static ServerTile air;
	public static ServerTile crackedWall0;
	public static ServerTile crackedWall1;
	public static ServerTile crackedWall2;
	public static ServerTile crackedWall3;
	public static ServerTile crackedWall4;
	public static ServerTile grass;
	public static ServerTile grassWithFlowers0;
	public static ServerTile sand;
	public static ServerTile wall;
	public static ServerTile water;
	public static ServerTile brokenWall;
	public static ServerTile destroyedWall;
	
	public static ServerTile wallProg0;
	public static ServerTile wallProg1;
	public static ServerTile wallProg2;
	public static ServerTile wallProg3;
	public static ServerTile wallProg4;
	public static ServerTile wallProg5;
	public static ServerTile wallProg6;
	public static ServerTile wallProg7;
	public static ServerTile wallProg8;
	public static ServerTile wallProg9;
	public static ServerTile wallProg10;
	public static ServerTile wallProg11;
	public static ServerTile wallProg12;
	public static ServerTile wallProg13;
	public static ServerTile wallProg14;
	
	public static ServerTile wallBlueprint;
	
	public static ServerTile rainbow;
	public static ServerTile chest;
	
	public static void initTiles()
	{
		air 					= new BaseTile(0, "", true, false, 0, 0).init();
		crackedWall0 			= new BreakableTile(1, "crackedWall0.png", true, 0, 0xff808080, 7, 11).createHitParticles().init();
		crackedWall1 			= new BreakableTile(2, "crackedWall1.png", true, 0, 0xff808080, 7, 11).createHitParticles().init();
		crackedWall2 			= new BreakableTile(3, "crackedWall2.png", true, 0, 0xff808080, 7, 11).createHitParticles().init();
		crackedWall3 			= new BreakableTile(4, "crackedWall3.png", true, 0, 0xff808080, 7, 11).createHitParticles().init();
		crackedWall4 			= new BreakableTile(5, "crackedWall4.png", true, 0, 0xff808080, 7, 11).createHitParticles().init();
		grass 					= new BaseTile(6, "grass.png", false, false, 0, 0xff006600).init();
		grassWithFlowers0 		= new BaseTile(7, "grassWithFlowers0.png", false, false, 0, 0xff007700).init();
		sand 					= new BaseTile(8, "sand.png", false, false, 0, 0xff7fa0a9).init();
		wall 					= new BreakableTile(9, "wall.png", true, 0, 0xff808080, 8, 11).createHitParticles().init();
		water 					= new BaseTile(10, "water.png", true, false, 0, 0xffbe5900).init();
		brokenWall 				= new BreakableTile(11, "wall1.png", true, 0, 0xff808080, 6, 12).createHitParticles().init();
		destroyedWall 			= new BaseTile(12, "destroyed_wall.png", false, false, 0, 0xff444444).init();
		
		wallProg0 				= new ProgressTile(13, "wall\\wall_progress.png", true, false, 0, 0xff808080, 0, 14, 12, 1).createHitParticles().init();
		wallProg1 				= new ProgressTile(14, "wall\\wall_progress.png", true, false, 0, 0xff808080, 1, 15, 12, 1).createHitParticles().init();
		wallProg2 				= new ProgressTile(15, "wall\\wall_progress.png", true, false, 0, 0xff808080, 2, 16, 12, 1).createHitParticles().init();
		wallProg3 				= new ProgressTile(16, "wall\\wall_progress.png", true, false, 0, 0xff808080, 3, 17, 12, 2).createHitParticles().init();
		wallProg4 				= new ProgressTile(17, "wall\\wall_progress.png", true, false, 0, 0xff808080, 4, 18, 12, 2).createHitParticles().init();
		wallProg5 				= new ProgressTile(18, "wall\\wall_progress.png", true, false, 0, 0xff808080, 5, 19, 12, 3).createHitParticles().init();
		wallProg6 				= new ProgressTile(19, "wall\\wall_progress.png", true, false, 0, 0xff808080, 6, 20, 12, 3).createHitParticles().init();
		wallProg7 				= new ProgressTile(20, "wall\\wall_progress.png", true, false, 0, 0xff808080, 7, 21, 12, 4).createHitParticles().init();
		wallProg8 				= new ProgressTile(21, "wall\\wall_progress.png", true, false, 0, 0xff808080, 8, 22, 12, 4).createHitParticles().init();
		wallProg9 				= new ProgressTile(22, "wall\\wall_progress.png", true, false, 0, 0xff808080, 9, 23, 12, 5).createHitParticles().init();
		wallProg10 				= new ProgressTile(23, "wall\\wall_progress.png", true, false, 0, 0xff808080, 10, 24, 12, 5).createHitParticles().init();
		wallProg11 				= new ProgressTile(24, "wall\\wall_progress.png", true, false, 0, 0xff808080, 11, 25, 12, 6).createHitParticles().init();
		wallProg12 				= new ProgressTile(25, "wall\\wall_progress.png", true, false, 0, 0xff808080, 12, 26, 12, 6).createHitParticles().init();
		wallProg13 				= new ProgressTile(26, "wall\\wall_progress.png", true, false, 0, 0xff808080, 13, 27, 12, 7).createHitParticles().init();
		wallProg14 				= new ProgressTile(27, "wall\\wall_progress.png", true, false, 0, 0xff808080, 14, 9, 12, 7).createHitParticles().init();
		
		wallBlueprint 			= new BlueprintTile(28, "wallBlueprint.png", 0xffff8080, 6, 13).init();
		
		rainbow 				= new BaseTile(29, "rainbow.png", true, true, 15, 0xff61f7ff).createHitParticles().init();
		
		chest 					= new Chest(30, "chest.png").init();
		
		atlas = new Atlas(sprites.toList());
		atlas.create(32, (x, y, i) -> tiles.getObject(i).setIndexes(x, y, i));
		GameTile.initGameTiles(getAtlas(), 32, 32);
		System.out.println("Tiles initialized");
	}
	
	public ServerTile init()
	{
		sprites.addObject(sprite);
		tiles.addObject(this);
		return this;
	}
	
	private void setIndexes(int x, int y, int index)
	{
		tiles.getObject(index).indexX = x;
		tiles.getObject(index).indexY = x;
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
	
	public int getMapColor()
	{
		return mapColor;
	}
	
	public static ServerTile getTile(int id)
	{
		return tiles.getObject(id);
	}
	
	public static SGArray<ServerTile> getTiles()
	{
		return tiles;
	}
	
	public static Atlas getAtlas()
	{
		return atlas;
	}
	
	public abstract void mouseEvent(int tx, int ty, PlayerMP player, int action, int button, GameWorld world);
	
	public abstract void enteredTile(int tx, int ty, PlayerMP player, GameWorld world);
	
	public abstract void leftTile(int tx, int ty, PlayerMP player, GameWorld world);
	
	public abstract void bulletCollision(int tx, int ty, Bullet bullet, GameWorld world);
}

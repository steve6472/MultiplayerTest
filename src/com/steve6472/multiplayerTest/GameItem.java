/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 15. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.main.game.inventory.Item;
import com.steve6472.sge.main.game.world.GameCamera;

public class GameItem extends Item
{
	private static final long serialVersionUID = 2629263788180843428L;
	
	public static GameItem sword = new GameItem(0, 1, 0, 0, 0, 0, ItemType.SWORD);
	public static GameItem thinSword = new GameItem(11, 1, 0, 0, 0, 0, ItemType.SWORD);
	public static GameItem dagger = new GameItem(0, 2, 0, 0, 0, 0, ItemType.DAGGER);

	public static GameItem changingSword = new GameItem(8, 0, 0, 0, 0, 0, ItemType.SWORD);
	public static GameItem changingSwordInv = new GameItem(5, 0, 0, 0, 0, 0, ItemType.SWORD);
	
	public static GameItem zappDagger = new GameItem(6, 2, 0, 0, 0, 0, ItemType.DAGGER);
	public static GameItem wallBlueprint = new GameItem(11, 2, 0, 0, 0, 0, ItemType.TILE);
	
	private int indexX;
	private int indexY;
	float colorRed;
	float colorGreen;
	float colorBlue;
	float colorAlpha;
	int mathMethod;
	
	public static GameItem[] items = new GameItem[256];
	private static int nextId = 0;
	
	public GameItem(int indexX, int indexY, float colorRed, float colorGreen, float colorBlue, float colorAlpha, ItemType itemType)
	{
		if (items == null)
		{
			items = new GameItem[256];
		}
		
		this.indexX = indexX;
		this.indexY = indexY;
		this.colorRed = colorRed;
		this.colorGreen = colorGreen;
		this.colorBlue = colorBlue;
		this.colorAlpha = colorAlpha;
		this.itemType = itemType;
		
		items[nextId++] = this;
	}
	
	public static void renderItem(int x, int y, GameItem item)
	{
		
	}
	
	public static void renderItemInWorld(int x, int y, float scale, float rot, GameItem item)
	{
		Helper.pushLayer();
		
		GameCamera camera = Game.camera;
		Helper.translate(camera.getX() + 32 * 16 - 16, camera.getY() + 32 * 9 - 16, 0);
		Helper.translate(-x, -y, 0);
		
		if (rot != 0)
			Helper.rotate(rot, 0, 0, 1);
		
		Helper.scale(16 + scale);
		Game.drawSpriteFromAtlas((float) item.getIndexX() / 16f, (float) item.getIndexY() / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
	}
	
	/*
	 * Multi layerd model
	 * 
	 * //Handle 
	 * Helper.color(0.5f, 0.25f, 0.125f, 0.0f);
	 * Game.drawSpriteFromAtlas(9f / 16f, 2f / 16f, model, shader, sprite);
	 * 
	 * //Blade 
	 * Helper.color(0.0f, 0.0f, 1.0f, 0.0f);
	 * Game.drawSpriteFromAtlas(10f / 16f, 2f / 16f, model, shader, sprite);
	 * 
	 * //Outline 
	 * Helper.color(0.0f, 0.0f, 0.0f, 0.0f);
	 * Game.drawSpriteFromAtlas(8f / 16f, 1f / 16f, model, shader, sprite);
	 */
	
	public int getIndexX()
	{
		return indexX;
	}
	
	public int getIndexY()
	{
		return indexY;
	}
}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import org.joml.Matrix4f;

import com.steve6472.sge.main.game.inventory.Item;

public class IngredientItem extends Item
{
	private static final long serialVersionUID = 5841255226670714810L;
	
	public static final IngredientItem redFlower0 = new IngredientItem(16, 48, 1f, 0f, 0f, 0f, 430302529l);
	public static final IngredientItem greenFlower0 = new IngredientItem(16, 48, 0f, 0.5f, 0f, 0f, 8675309l);
	public static final IngredientItem blueFlower0 = new IngredientItem(16, 48, 0f, 0f, 1f, 0f, 505289941l);
	
	public static final IngredientItem redFlower1 = new IngredientItem(17, 48, 1f, 0f, 0f, 0f, 136191533l);
	public static final IngredientItem greenFlower1 = new IngredientItem(17, 48, 0f, 0.5f, 0f, 0f, 160734541l);
	public static final IngredientItem blueFlower1 = new IngredientItem(17, 48, 0f, 0f, 1f, 0f, 944846988l);
	
	public static final IngredientItem redFlowerPetal0 = new IngredientItem(18, 48, 1f, 0f, 0f, 0f, 878049860l);
	public static final IngredientItem greenFlowerPetal0 = new IngredientItem(18, 48, 0f, 0.5f, 0f, 0f, 214905339l);
	public static final IngredientItem blueFlowerPetal0 = new IngredientItem(18, 48, 0f, 0f, 1f, 0f, 7244868l);
	
	public static final IngredientItem redSeeds0 = new IngredientItem(16, 49, 1f, 0f, 0f, 0f, 854605830l);
	public static final IngredientItem redSeeds1 = new IngredientItem(17, 49, 1f, 0f, 0f, 0f, 719396242L);
	public static final IngredientItem redSeeds2 = new IngredientItem(18, 49, 1f, 0f, 0f, 0f, 484855876981l);
	
	public static final IngredientItem greenSeeds0 = new IngredientItem(16, 49, 0f, 0.5f, 0f, 0f, 149507519l);
	public static final IngredientItem greenSeeds1 = new IngredientItem(17, 49, 0f, 0.5f, 0f, 0f, 411621364l);
	public static final IngredientItem greenSeeds2 = new IngredientItem(18, 49, 0f, 0.5f, 0f, 0f, 876222344l);
	
	public static final IngredientItem blueSeeds0 = new IngredientItem(16, 49, 0f, 0f, 1f, 0f, 889328396l);
	public static final IngredientItem blueSeeds1 = new IngredientItem(17, 49, 0f, 0f, 1f, 0f, 329574554l);
	public static final IngredientItem blueSeeds2 = new IngredientItem(18, 49, 0f, 0f, 1f, 0f, 765197389l);
	
	public static IngredientItem[] items = new IngredientItem[256];
	private static int nextId = 0;
	
	int indexX;
	int indexY;
	float colorRed;
	float colorGreen;
	float colorBlue;
	float colorAlpha;
	long randomValue;
	int mathMethod;
	
	public IngredientItem(int indexX, int indexY, float colorRed, float colorGreen, float colorBlue, float colorAlpha, long randomValue)
	{
		if (items == null)
		{
			items = new IngredientItem[256];
		}
		
		this.indexX = indexX;
		this.indexY = indexY;
		this.colorRed = colorRed;
		this.colorGreen = colorGreen;
		this.colorBlue = colorBlue;
		this.colorAlpha = colorAlpha;
		this.randomValue = randomValue;
		determineMathMethod(randomValue);
		
		items[nextId++] = this;
	}
	
	public static void renderItem(float x, float y, IngredientItem item, Matrix4f worldMat, float scale)
	{
		if (item == null)
			return;
		
		float tileIndexX = item.indexX / 512f;
		float tileIndexY = item.indexY / 512f;
		
		float X = x - Game.camera.getX();
		float Y = y - Game.camera.getY();
		
		Helper.pushLayer();
		
		Helper.translate(X * -.25f - 4f, Y * -.25f - 4f, 0);
		Helper.scale(scale);
		Helper.color(item.colorRed, item.colorGreen, item.colorBlue, item.colorAlpha);
		
		Game.drawSpriteFromAtlas(tileIndexX * 8, tileIndexY * 8, Game.pixelModel8, Game.shader, Game.sprites);
		
		Helper.popLayer();
		
//		Matrix4f tilePos = new Matrix4f()
//				.translate(X * -0.25f - 4, Y * -0.25f - 4, 0)
//				.scale(scale);
//		Matrix4f target = new Matrix4f();
//		
//		Shader shader = MultiplayerTest.shader;
//		Camera camera = MultiplayerTest.camera;
//		
//		camera.getProjection().mul(worldMat, target);
//		target.mul(tilePos);
//		
//		shader.setUniform2f("texture", tileIndexX * 8, tileIndexY * 8);
//		
//		shader.setUniformMat4f("projection", target);
//		shader.setUniform4f("col", item.colorRed, item.colorGreen, item.colorBlue, item.colorAlpha);
//		
//		MultiplayerTest.pixelModel8.render();
	}
	
	private void determineMathMethod(long val)
	{
		String sVal = String.valueOf(val);
		int firstChar = Integer.parseInt(sVal.substring(0, 1));
		if (firstChar < 5)
		{
			mathMethod = Integer.parseInt(sVal.substring(1, 2));
		} else
		{
			mathMethod = Integer.parseInt(sVal.substring(2, 3));
		}
	}
	
	public long useMathMethod(long finalValue)
	{
		if (mathMethod == 1)
			return randomValue + finalValue;
		else if (mathMethod == 2)
			return randomValue - finalValue;
		else if (mathMethod == 3)
			return randomValue * finalValue;
		else if (mathMethod == 4)
			return randomValue / finalValue;
		else if (mathMethod == 5)
			return finalValue - randomValue;
		else if (mathMethod == 6)
			return finalValue / randomValue;
		else if (mathMethod == 7)
			return (long) Math.sqrt((double) (randomValue - finalValue));
		else if (mathMethod == 8)
			return (long) Math.sqrt((double) (finalValue - randomValue));
		else // mathMethod == 9
			return (randomValue * finalValue) * (randomValue * finalValue);
	}
}

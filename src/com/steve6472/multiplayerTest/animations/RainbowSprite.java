/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 17. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameItem;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.animations.KeyframedAnimation;
import com.steve6472.sge.main.game.world.GameCamera;

public class RainbowSprite extends KeyframedAnimation
{
	GameItem item;
	float xInWorld, yInWorld;
	float colorBrightness;
	
	public RainbowSprite(GameItem item, float xInWorld, float yInWorld, float colorBrightness)
	{
		this.item = item;
		this.xInWorld = xInWorld;
		this.yInWorld = yInWorld;
		this.colorBrightness = colorBrightness;
	}

	@Override
	public void setKeyFrames()
	{
		float c = colorBrightness;
		newFrame(0 ).color(0, 0, 0, 0).scale(32).finish(Mode.SET, Mode.SET);
		newFrame(60).color(c, 0, 0, 0).finish(Mode.SET);
		newFrame(60).color(0, c, 0, 0).finish(Mode.SET);
		newFrame(60).color(0, 0, c, 0).finish(Mode.SET);
		newFrame(60).color(0, c, c, 0).finish(Mode.SET);
		newFrame(60).color(c, c, 0, 0).finish(Mode.SET);
		newFrame(60).color(c, 0, c, 0).finish(Mode.SET);
		newFrame(60).color(c, c, c, 0).finish(Mode.SET);
		newFrame(60).color(0, 0, 0, 0).finish(Mode.SET);
	}

	@Override
	protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang, float rx,
			float ry, float rz, Sprite sprite, Model model, Shader shader)
	{
		Helper.pushLayer();
		
		GameCamera camera = Game.camera;
		Helper.translate(camera.getX() + 32 * 16 - 16, camera.getY() + 32 * 9 - 16, 0);
		Helper.translate(-x, -y, 0);
		
		Helper.color(r, g, b, a);
		
		Helper.rotate(ang, rx, ry, rz);
		
		Helper.scale(sx, sy, sz);
		Game.drawSpriteFromAtlas((float) item.getIndexX() / 16f, (float) item.getIndexY() / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
	}
	
	@Override
	public int getId()
	{
		return 0;
	}

}

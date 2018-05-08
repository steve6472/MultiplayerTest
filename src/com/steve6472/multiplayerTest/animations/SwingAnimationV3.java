/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 17. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import static com.steve6472.sge.gfx.animations.KeyframedAnimation.Mode.*;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameItem;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.animations.IAnimationTick;
import com.steve6472.sge.gfx.animations.KeyframedAnimation;
import com.steve6472.sge.main.Util;

public class SwingAnimationV3 extends KeyframedAnimation implements IAnimationTick
{
	GameItem item;
	float angle, x, y;
	
	public SwingAnimationV3(GameItem item)
	{
		this.item = item;
	}
	
	@Override
	public void setKeyFrames()
	{
		long d = 2;
		newFrame(0 ).scale(16).finish(SET);
		newFrame(d).rotate(5, 0, 0, 1).translate(-2, 0, 0).finish(CHANGE, SET);
		newFrame(d * 2).rotate(-95, 0, 0, 1).finish(CHANGE);
		newFrame(d * 2).rotate(-90, 0, 0, 1).finish(SET);
		newFrame(d).rotate(-80, 0, 0, 1).finish(SET);
		newFrame(d).rotate(-20, 0, 0, 1).finish(SET);
		newFrame(d).rotate(0, 0, 0, 1).finish(CHANGE);
	}
	
	@Override
	public void tick(float angle, float x, float y)
	{
		this.angle = angle;
		this.x = x;
		this.y = y;
	}
	
	@Override
	protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang, float rx,
			float ry, float rz, Sprite sprite, Model model, Shader shader)
	{
		float rotPoint = 30;
		
		int t = (int) time;
		
		if (Util.isNumberInRange(2, 6, t))
		{
			Helper.pushLayer();

			Helper.rotate(angle, 0, 0, 1);
			Helper.translate(x, y, z);
			Helper.translate(3, 27, 0);
			Helper.scale(32);

			Game.drawSpriteFromAtlas(7f / 8f, 0f / 8f, Game.pixelModel64, Game.shader, Game.sprites);

			Helper.popLayer();

		}

		Helper.pushLayer();

		Helper.rotate(angle, 0, 0, 1);
		Helper.translate(x, y, z);
		Helper.translate(0, 24, 0);
		Helper.color(r, g, b, a);
		Helper.scale(sx, sy, sz);
		float rt = rotPoint / 32f;
		Helper.translate(-rt, -rt, 0);
		Helper.rotate(ang + 45, rx, ry, rz);
		Helper.translate(rt, rt, 0);
		
		Game.drawSpriteFromAtlas((float) item.getIndexX() / 16f, (float) item.getIndexY() / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
	}
	
	@Override
	public int getId()
	{
		return 0;
	}

}

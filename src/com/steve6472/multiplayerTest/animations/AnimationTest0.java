/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 18. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import static com.steve6472.sge.gfx.animations.KeyframedAnimation.Mode.*;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.animations.KeyframedAnimation;

public class AnimationTest0 extends KeyframedAnimation
{
	KFA head, footF, footB;
	
	public AnimationTest0()
	{
		head = new KFA()
		{
			@Override
			protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang,
					float rx, float ry, float rz, Sprite sprite, Model model, Shader shader)
			{
				Helper.pushLayer();
				Helper.translate(x, y, z);
				Helper.rotate(ang, rx, ry, rz);
				Helper.translate(18, 8, 0);
				Helper.scale(sx, sy, sz);
				
				Game.drawSpriteFromAtlas(4f / 64f, 13f / 64f, model, shader, sprite);
				
				Helper.popLayer();
			}
		};
		head.newFrame(0 ).translate(0, 100, 0).scale(8).sprite(Game.sprites).model(Game.pixelModel8).shader(Game.shader).finish();
		head.newFrame(5 ).rotate(5, 0f, 0f, 1f).finish(SET);
		head.newFrame(20 ).finish();
		head.newFrame(10 ).rotate(-5, 0f, 0f, 1f).finish(SET);
		head.newFrame(20 ).finish();
		head.newFrame(5  ).rotate(0, 0f, 0f, 1f).finish(SET);
		
		footF = new KFA()
		{
			@Override
			protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang,
					float rx, float ry, float rz, Sprite sprite, Model model, Shader shader)
			{
				Helper.pushLayer();
				Helper.translate(x, y, z);
				Helper.rotate(ang, rx, ry, rz);
				Helper.translate(10, -14, 0);
				Helper.rotate(ang * 2, rx, ry, rz);
				Helper.scale(sx, sy, sz);
				
				Game.drawSpriteFromAtlas(4f / 64f, 12f / 64f, model, shader, sprite);
				
				Helper.popLayer();
			}
		};
		footF.newFrame(0 ).translate(0, 100, 0).scale(8).sprite(Game.sprites).model(Game.pixelModel8).shader(Game.shader).finish();
		footF.newFrame(5 ).rotate(5, 0f, 0f, 1f).finish(SET);
		footF.newFrame(20 ).finish();
		footF.newFrame(10 ).rotate(-5, 0f, 0f, 1f).finish(SET);
		footF.newFrame(20 ).finish();
		footF.newFrame(5  ).rotate(0, 0f, 0f, 1f).finish(SET);
		
		footB = new KFA()
		{
			@Override
			protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang,
					float rx, float ry, float rz, Sprite sprite, Model model, Shader shader)
			{
				Helper.pushLayer();
				Helper.translate(x, y, z);
				Helper.rotate(ang, rx, ry, rz);
				Helper.translate(-16, -14, 0);
				Helper.scale(sx, sy, sz);
				Helper.rotate(ang * 2, rx, ry, rz);
				
				Game.drawSpriteFromAtlas(4f / 64f, 12f / 64f, model, shader, sprite);
				
				Helper.popLayer();
			}
		};
		footB.newFrame(0 ).translate(0, 100, 0).scale(8).sprite(Game.sprites).model(Game.pixelModel8).shader(Game.shader).finish();
		footB.newFrame(5 ).rotate(5, 0f, 0f, 1f).finish(SET);
		footB.newFrame(20 ).finish();
		footB.newFrame(10 ).rotate(-5, 0f, 0f, 1f).finish(SET);
		footB.newFrame(20 ).finish();
		footB.newFrame(5  ).rotate(0, 0f, 0f, 1f).finish(SET);
	}

	@Override
	public void setKeyFrames()
	{
		newFrame(0  ).translate(0, 100, 0).scale(32).sprite(Game.sprites).model(Game.pixelModel32).shader(Game.shader).finish();
		newFrame(5 ).rotate(5, 0f, 0f, 1f).finish(SET);
		newFrame(20 ).finish();
		newFrame(10 ).rotate(-5, 0f, 0f, 1f).finish(SET);
		newFrame(20 ).finish();
		newFrame(5  ).rotate(0, 0f, 0f, 1f).finish(SET);
	}

	@Override
	public void tick()
	{
		super.tick();
		head.tick();
		footF.tick();
		footB.tick();
	}
	
	@Override
	protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang, float rx,
			float ry, float rz, Sprite sprite, Model model, Shader shader)
	{
		Helper.pushLayer();
		Helper.translate(x, y, z);
		Helper.rotate(ang, rx, ry, rz);
		Helper.scale(sx, sy, sz);
		
		Game.drawSpriteFromAtlas(0f / 16f, 3f / 16f, model, shader, sprite);
		
		Helper.popLayer();
		
		head.render();
		footF.render();
		footB.render();
	}
	
	@Override
	public int getId()
	{
		return 0;
	}
}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.animations.KeyframedAnimation;

public class SlideAnimation extends KeyframedAnimation
{

	@Override
	public void setKeyFrames()
	{
		newFrame(0).scale(32).translate(0, 200, 0).finish(Mode.CHANGE, Mode.CHANGE);
		newFrame(200).translate(400, 200, 0).addBezierCurvePoint(800, 200, 0).finish(Mode.BRAZIER_SET);
		newFrame(100).finish();
	}

	@Override
	protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang, float rx,
			float ry, float rz, Sprite sprite, Model model, Shader shader)
	{
		Helper.pushLayer();
		
		Helper.translate(x, y, z);
		
		Helper.scale(sx, sy, sz);
		Game.drawSquare(x, y, sx, sy, 0xff00ff00);
//		Game.drawSpriteFromAtlas(4f / 16f, 0f / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
	}
	
	@Override
	public int getId()
	{
		return 0;
	}
	
}

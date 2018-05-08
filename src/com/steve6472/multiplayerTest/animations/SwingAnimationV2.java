/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 15. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import static com.steve6472.sge.gfx.animations.KeyframedAnimation.Mode.ADD;
import static com.steve6472.sge.gfx.animations.KeyframedAnimation.Mode.BRAZIER_ADD;
import static com.steve6472.sge.gfx.animations.KeyframedAnimation.Mode.BRAZIER_SET;
import static com.steve6472.sge.gfx.animations.KeyframedAnimation.Mode.SET;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.animations.KeyframedAnimation;
import com.steve6472.sge.main.SGArray;

@SuppressWarnings("unused")
public class SwingAnimationV2 extends KeyframedAnimation
{

	public static int d = 20;
	
	@Override
	public void setKeyFrames()
	{
		//Initial values
//		new KeyFrame(1 ).color_(0, 0, 0, 0)		.finish(SET);
//		
//		new KeyFrame(60).color_(1, 0, 0, 0)		.finish(SET);//0
//		new KeyFrame(60).translate_(64, 64, 0)	.finish(SET);//1
//		new KeyFrame(60).color_(0, 1, 0, 0)		.finish(SET);//2
//		new KeyFrame(60).translate_(-128, 0, 0)	.finish(ADD);//3
//		new KeyFrame(60).color_(0, 0, 1, 0)		.finish(SET);//4
		
//		new KeyFrame(0 ).color_(0, 0, 0, 0).finish();
//		new KeyFrame(60).color_(1, 1, 1, 0).finish();
//		new KeyFrame(60).translate_(0, 0, 0).finish();
//		new KeyFrame(60).translate_(10, 0, 0).finish();
		
//		int d = 20;
		
//		new KeyFrame(0).translate_(-64, -64, 0).finish(SET);
//		new KeyFrame(d).color_(1, 0, 0, 0).finish(SET);
//		new KeyFrame(d).translate_(128, 0, 0).finish(ADD);
//		new KeyFrame(d).color_(0, 1, 0, 0).finish(SET);
//		new KeyFrame(d).translate_(0, 128, 0).finish(ADD);
//		new KeyFrame(d).color_(0, 0, 1, 0).finish(SET);
//		new KeyFrame(d).translate_(-128, 0, 0).finish(ADD);
//		new KeyFrame(d).color_(1, 0, 1, 0).finish(SET);
//		new KeyFrame(d).translate_(0, -128, 0).finish(ADD);
//		new KeyFrame(d).color_(0, 0, 0, 0).finish(SET);
//		new KeyFrame(d).finish(SET);
		float distance = 300;
		newFrame(0 ).translate(distance, 0, 0).color(1, 1, 1, 0).scale(32).sprite(Game.sprites).model(Game.pixelModel32).shader(Game.shader).finish(SET);
		newFrame(30).scale(64, 64, 32).finish(SET);
		newFrame(30).scale(32, 64, 32).finish(SET);
		newFrame(30).scale(32).finish(SET);
		newFrame(60).rotate(180, 0, 0, 1).color(1, 0, 0, 0).translate(128, 0, 0).finish(SET, SET, ADD);
		newFrame(60).rotate(0, 0, 0, 1).color(0, 1, 0, 0).translate(-128, 0, 0).finish(SET, SET, ADD);
		newFrame(60).rotate(180, 0, 0, 1).color(0, 0, 1, 0).finish(SET, SET);
		newFrame(60).rotate(0, 0, 0, 1).color(0, 1, 0, 0).finish(SET, SET);
		newFrame(10).rotate(90, 0, 0, 1).finish(ADD);
		newFrame(30).finish();
		newFrame(10).rotate(90, 0, 0, 1).finish(ADD);
		newFrame(30).finish();
		newFrame(10).rotate(90, 0, 0, 1).finish(ADD);
		newFrame(30).finish();
		newFrame(10).rotate(90, 0, 0, 1).finish(ADD);
		newFrame(30).finish();
		newFrame(1).rotate(0, 0, 0, 1).finish(SET);
		newFrame(60).translate(-distance, 0, 0).color(0, 0, 0, 0).addBezierCurvePoint(distance, 256, 0).finish(BRAZIER_SET, SET);
	}

	@Override
	protected void render(long time, float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang, float rx,
			float ry, float rz, Sprite sprite, Model model, Shader shader)
	{
		Helper.pushLayer();
		Helper.translate(x, y, z);
		Helper.rotate(ang, rx, ry, rz);
		Helper.scale(sx, sy, sz);
		Helper.color(r, g, b, a);
		Game.drawSpriteFromAtlas(8f / 16f, 0, model, shader, sprite);
		
		Helper.popLayer();
	}
	
	@Override
	public int getId()
	{
		return 0;
	}
}

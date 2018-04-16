/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 15. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.Helper;

import static com.steve6472.multiplayerTest.animations.KeyframedAnimation.Mode.SET;
import static com.steve6472.multiplayerTest.animations.KeyframedAnimation.Mode.ADD;
import static com.steve6472.multiplayerTest.animations.KeyframedAnimation.Mode.BRAZIER_ADD;
import static com.steve6472.multiplayerTest.animations.KeyframedAnimation.Mode.BRAZIER_SET;

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
		new KeyFrame(0 ).translate_(distance, 0, 0).color_(1, 1, 1, 0).scale_(32).finish(SET);
		new KeyFrame(30).scale_(64, 64, 32).finish(SET);
		new KeyFrame(30).scale_(32, 64, 32).finish(SET);
		new KeyFrame(30).scale_(32).finish(SET);
		new KeyFrame(60).rotate_(180, 0, 0, 1).color_(1, 0, 0, 0).finish(SET, SET);
		new KeyFrame(60).rotate_(0, 0, 0, 1).color_(0, 1, 0, 0).finish(SET, SET);
		new KeyFrame(60).rotate_(180, 0, 0, 1).color_(0, 0, 1, 0).finish(SET, SET);
		new KeyFrame(60).rotate_(0, 0, 0, 1).color_(1, 1, 1, 0).finish(SET, SET);
		new KeyFrame(20).finish();
	}

	@Override
	protected void render(float x, float y, float z, float r, float g, float b, float a, float sx, float sy, float sz, float ang, float rx, float ry,
			float rz)
	{
		Helper.pushLayer();
		Helper.translate(x, y, z);
		Helper.rotate(ang, rx, ry, rz);
		Helper.scale(sx, sy, sz);
		Helper.color(r, g, b, a);
		Game.drawSpriteFromAtlas(4f / 16f, 0, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
	}
}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 15. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameItem;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.animations.Animation;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.Vec2;

public class SwingAnimation extends Animation
{

	@Override
	public void render()
	{
		renderItem(GameItem.sword, time * 360f / 120f);
	}
	
	float p0x = 0, p0y = 9 * 32 * 2;
	float p1x = 16 * 32, p1y = 0;
	float p2x = 16 * 32 * 2, p2y = 9 * 32 * 2;
	
	long maxTime = 120 * 4;
	
	SGArray<Vec2> points;
	
	public SwingAnimation()
	{
		points = new SGArray<Vec2>(0, true, true);
	}
	
	private void renderItem(GameItem item, float rot)
	{
//		drawLine(p0x, p0y, p1x, p1y, 0xff303030);
//		drawLine(p1x, p1y, p2x, p2y, 0xff303030);
		
		float t0 = time;
		float t1 = maxTime;

		float x0 = Util.calculateValue(t0, t1, p0x, p1x);
		float y0 = Util.calculateValue(t0, t1, p0y, p1y);

		float x1 = Util.calculateValue(t0, t1, p1x, p2x);
		float y1 = Util.calculateValue(t0, t1, p1y, p2y);
		
//		drawLine(x0, y0, x1, y1, 0xff00ff00);

		float px = Util.calculateValue(t0, t1, x0, x1);
		float py = Util.calculateValue(t0, t1, y0, y1);
		
		points.addObject(new Vec2(px, py));
		
//		for (Vec2 v : points)
//		{
//			Game.drawSquare((float) v.getX(), (float) v.getY(), 1, 1, 0xffff0000);
//		}
		
		
		float g = bezierMethod(3f, (float) time / (float) maxTime, 0, 1);
		
		Helper.pushLayer();
		
		Helper.translate(32, g * 10f, 0);
		
		Helper.scale(4);
		Game.drawSpriteFromAtlas(0, 0, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
		
		
		
		Helper.pushLayer();
		
		Helper.color(g, g, g, 0);
		
		Helper.scale(16);
		
//		Game.drawSpriteFromAtlas((float) item.getIndexX() / 16f, (float) item.getIndexY() / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		Game.drawSpriteFromAtlas(4f/ 16f, 0f / 16f, Game.pixelModel32, Game.shader, Game.sprites);
		
		Helper.popLayer();
	}
	

	private static float bezierMethod(float point, float time, float lastColor, float nextColor)
	{
		return Util.bezierCurve(lastColor, 0.30f + point, nextColor, time, 1);
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, int color)
	{
		float a = (float) (y2 - y1);
		float b = (float) (x2 - x1);
		float c = (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

		double ang = Math.toRadians(Util.countAngle(x1, y1, x2, y2) - 90);

		float cos = (float) Math.cos(ang);
		float sin = (float) Math.sin(ang);

		for (int i = 0; i < c; i++)
		{
			float X = x1 + cos * i;
			float Y = y1 + sin * i;
			Game.drawSquare(X, Y, 1, 1, color);
		}
	}
	
	@Override
	public boolean hasEnded()
	{
		return time >= maxTime;
	}
	
	@Override
	public int getId()
	{
		return 0;
	}
	
}

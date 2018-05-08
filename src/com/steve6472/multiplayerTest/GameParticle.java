/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 28. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import org.joml.Matrix4f;

import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.main.SArray;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.game.Atlas;
import com.steve6472.sge.main.game.Vec2;
import com.steve6472.sge.main.game.particle.AngledParticle;
import com.steve6472.sge.main.game.world.GameCamera;
import com.steve6472.sge.test.Camera;

public class GameParticle extends AngledParticle
{
	private static final long serialVersionUID = -8771601191045760439L;
	
	float indexX, indexY, sizeX, sizeY;
	
	/*
	 * INCLUDE SARRAY FOR CUSTOM VARIABLES INSTEAD OF var1 var2 var3 AND SO ON
	 */
	
	public SArray bigData;
	public SArray smallData;
	
	public float rotation;
	
	private static IMove[] move;
	private static IMove[] smallMove;
	
	static
	{
		move = new IMove[]
				{
						new MoveInStraightLine(),
						new MoveInStraightLineR(),
						new MoveRotation(),
						new MoveRotationR(),
						new MoveCircle(),
						new MoveVertex()
				};
		
		smallMove = new IMove[]
				{
						new Move(),
						new MoveRandom(),
				};
		
	}
	
	Vec2 originalLocation;
	
	int moveStyle = 0;
	int smallMoveStyle = 0;
	
	int maxLife = 0;
	
	public GameParticle(double x, double y, double angle, int life, int hitId, int bigMoveStyle, int smallMoveStyle, long seed)
	{
		super(x, y, angle, life, null);
		maxLife = life;
		originalLocation = new Vec2(x, y);
		Atlas a = ClientGui.getAtlas();
		int hitX = hitId % a.getSize();
		int hitY = hitId / a.getSize();
		indexX = hitX / (float) a.getSize();
		indexY = hitY / (float) a.getSize();
		this.moveStyle = bigMoveStyle;
		this.smallMoveStyle = smallMoveStyle;
		
		bigData = new SArray(0, true, false);
		smallData = new SArray(0, true, false);
		
		move[this.moveStyle].initData(bigData, seed, this);
		
		smallMove[this.smallMoveStyle].initData(smallData, seed, this);
	}

	@Override
	protected void move()
	{
		if (!isDead())
		{
			moveBig();
			smallMove[smallMoveStyle].move(pos, getAngle(), 1.48d, this, smallData);
			life--;
			if (life <= 0)
				setDead();
		}
	}
	
	boolean render = true;
	boolean revertVisibility = false;
	
	public void moveBig()
	{
		move[moveStyle].move(pos, getAngle(), 1.48d, this, bigData);
	}
	

	@Override
	public void render(Screen screen)
	{
		if (!render)
			return;
		
		GameCamera camera = Game.camera;
		
		Helper.pushLayer();
		
		//Render particle in center of the screen
		Helper.translate(1f / (float) camera.getWidth() + camera.getWidth() / 2, 1f / (float) camera.getHeight() + camera.getHeight() / 2, 0);
		//Move particle by players position in the world
		Helper.translate(-getIntX() + camera.getX(), -getIntY() + camera.getY(), 0);
		//Scale it so we can actually see it
		Helper.scale(4);
		
		//Change visibility
		if (!revertVisibility)
			Helper.color(0.0f, 0.0f, 0.0f, (1f / (float) maxLife) * (float) life - 0.95f);
		else
			Helper.color(0.0f, 0.0f, 0.0f, -(1f / (float) maxLife) * (float) life);

		//Rotate
		if (rotation != 0)
			Helper.rotate(rotation, 0, 0, 1);

		//& Finally draw
		drawSpriteFromAtlas(indexX, indexY, Game.tileModel);
		
		Helper.popLayer();
	}
	
	private static void drawSpriteFromAtlas(float indexX, float indexY, Model model)
	{
		if (!Helper.isInitialised())
			return;
		
		Matrix4f target = new Matrix4f();
		
		Camera camera = Game.camera;

		camera.getProjection().mul(Helper.toMatrix(), target);
		
		Shader shader = Game.shader;

		shader.setUniform2f("texture", indexX, indexY);
		shader.setUniform4f("col", Helper.getRed(), Helper.getGreen(), Helper.getBlue(), Helper.getAlpha());

		shader.setUniformMat4f("projection", target);

		model.render();
	}
}

interface IMove
{
	public void initData(SArray data, long seed, GameParticle particle);
	
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data);
}

class MoveInStraightLine implements IMove
{
	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
		data.addDouble(1.48d);
	}
	
	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
		location.move2(angle, data.getDouble(0));
	}
}

class MoveInStraightLineR implements IMove
{
	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
	}
	
	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
		location.move2(angle - 180 + 360, speed);
	}
}

class MoveRotation implements IMove
{
	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
		data.addDouble(0);
	}
	
	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
		particle.setAngle(angle + data.getDouble(0));
//		data.setDouble(0, data.getDouble(0) + 0.1d);
		data.matDouble(0, 0.1d);
		location.move2(angle, speed);
	}
}

class MoveRotationR implements IMove
{
	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
		data.addDouble(0);
	}
	
	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
		particle.setAngle(angle + data.getDouble(0));
//		data.setDouble(0, data.getDouble(0) - 0.1d);
		data.matDouble(0, -0.1d);
		location.move2(angle, speed);
	}
}

class MoveCircle implements IMove
{
	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
		data.addDouble(0); 		//var	0
		data.addDouble(0); 		//var2	1
		data.addDouble(0); 		//var3	2
		data.addDouble(1.18d); 	//var4	3
	}
	
	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
		
		if (data.getDouble(1) <= 40)
		{
			data.matDouble(2, -0.001d);
			data.matDouble(1, data.getDouble(3) + data.getDouble(2));
		}
		double radius = data.getDouble(1) * 2d;
		particle.setAngle(angle + 360d / (radius * 4d / speed));
		double x1 = radius * Math.cos(angle * Math.PI / 180);
		double y1 = radius * Math.sin(angle * Math.PI / 180);
		location.setLocation(x1 + particle.originalLocation.getX(), y1 + particle.originalLocation.getY());
		data.matDouble(0, speed);
	}
}

class MoveVertex implements IMove
{
	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
		particle.revertVisibility = true;
		
		data.addDouble(Util.getRandomDouble(360, 0, seed)); //radius	0
		data.addDouble(Util.getRandomDouble(180, 0, seed)); //angle		1
		data.addBoolean(true);								//New		2
	}
	
	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
		double radius = Util.maxd(1d, data.getDouble(0));
		if (data.getBoolean(2))
		{
			data.setBoolean(2, false);
			double ang = Util.getRandomAngle();
			double x1 = radius * Math.cos(ang * Math.PI / 180d);
			double y1 = radius * Math.sin(ang * Math.PI / 180d);
			location.setLocation(x1 + particle.originalLocation.getX(), y1 + particle.originalLocation.getY());
		} else
		{
			double ang = Util.countAngle(location.getX(), location.getY(), particle.originalLocation.getX(), particle.originalLocation.getY()) + data.getDouble(1);
			location.move2(ang + angle, speed);
		}
		particle.rotation -= Util.getRandomFloat(3f, 2f);
	}
}

class Move implements IMove
{

	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
	}

	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
	}
	
}

class MoveRandom implements IMove
{

	@Override
	public void initData(SArray data, long seed, GameParticle particle)
	{
		//Angle
		data.addDouble(0);
		//Move Range
		data.addDouble(Util.getRandomDouble(1, 0, seed));
	}

	@Override
	public void move(Vec2 location, double angle, double speed, GameParticle particle, SArray data)
	{
		double ang = data.getDouble(0);
		
		double moveRange = data.getDouble(1);
		double move = Util.getRandomDouble(0.1, 0.08);
		
		if (move > moveRange)
			move = moveRange;
		if (move < -moveRange)
			move = -moveRange;
		
		switch (Util.getRandomInt(2, 0))
		{
		case 0:
			ang -= move;
			break;
		case 1:
			ang += move;
			break;
		}
		
		particle.setAngle(angle + ang);

		data.setDouble(0, ang);
	}
	
}

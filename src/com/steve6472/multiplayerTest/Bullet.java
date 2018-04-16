/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.BaseEntity;

public class Bullet extends BaseEntity
{
	private static final long serialVersionUID = 7597621538512588912L;
	
	private int networkId;
	private int shooterNetworkId;

	@Override
	public void render(Screen screen)
	{
		Game.drawSquare(loc.getIntX() - Game.camera.getX(), loc.getIntY() - Game.camera.getY(), 3, 3, 0xffff0000);
	}
	
	private int life = 0;
	public double drag = 1;
	
	@Override
	public void tick()
	{
		move();
		speed *= drag;
		life++;
		if (life >= 90)
			setDead();
	}

	@Override
	public void initEntity(MainApplication game, Object... objects)
	{
		//					Network Id
		checkClass(objects, Integer.class, Integer.class);
		
		networkId = (int) objects[0];
		shooterNetworkId = (int) objects[1];
	}

	@Override
	public Sprite setSprite()
	{
		return new Sprite(new int[] {0xffff0000, 0xffff0000, 0xffff0000,
									 0xffff0000, 0xffff0000, 0xffff0000,
									 0xffff0000, 0xffff0000, 0xffff0000}
																		, 3, 3);
	}
	
	public int getNetworkId()
	{
		return networkId;
	}
	
	public int getShooterNetworkId()
	{
		return shooterNetworkId;
	}

}

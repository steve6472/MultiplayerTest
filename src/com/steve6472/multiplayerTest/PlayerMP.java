/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.net.InetAddress;

import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.AABB;
import com.steve6472.sge.main.game.BaseEntity;
import com.steve6472.sge.main.game.Vec2;

public class PlayerMP extends BaseEntity
{
	private static final long serialVersionUID = -2737716142975997153L;
	private InetAddress ip;
	private int port;
	private int networkId;
	
	public Vec2 lastValidLocation = new Vec2();
	public int score = 0;
	
	private static int nextNetworkId;

	/**
	 * Server init
	 * @param ip
	 * @param port
	 * @param x
	 * @param y
	 * @param networkId
	 */
	public PlayerMP(InetAddress ip, int port, int x, int y)
	{
		this.ip = ip;
		this.port = port;
		this.networkId = nextNetworkId++;
		this.setLocation(x, y);
	}
	
	/**
	 * Client init
	 * @param x
	 * @param y
	 * @param networkId
	 */
	public PlayerMP(int x, int y, int networkId)
	{
		this.setLocation(x, y);
		this.networkId = networkId;
	}

	@Override
	public void render(Screen screen)
	{
	}

	@Override
	public void tick()
	{
	}

	@Override
	public void initEntity(MainApplication game, Object... objects)
	{
	}
	
	public void updateBox()
	{
		setBox(new AABB(loc.getX(), loc.getY(), loc.getX() + 32, loc.getY() + 32));
	}

	@Override
	public Sprite setSprite()
	{
		return null;
	}

	public int getPort()
	{
		return port;
	}
	
	public InetAddress getIp()
	{
		return ip;
	}
	
	public void setIp(InetAddress ip)
	{
		this.ip = ip;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

	public int score()
	{
		score++;
		return score;
	}

}

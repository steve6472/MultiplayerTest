/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 28. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.test.Camera;

public class GameCamera extends Camera
{
	public int x;
	public int y;
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void setLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}

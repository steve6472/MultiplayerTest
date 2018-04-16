/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 15. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.animations;

public abstract class Animation
{
	protected long time = 0;
	
	public abstract void render();
	
	public abstract boolean hasEnded();
	
	public void tick()
	{
		time++;
	}
}

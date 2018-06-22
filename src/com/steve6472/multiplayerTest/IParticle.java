/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 22. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;

public interface IParticle
{
	public void tick();
	
	public void render();
	
	public boolean isDead();
	
	public Sprite getSprite();
	
	public Shader getShader();
}

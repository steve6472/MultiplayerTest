/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 22. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.multiplayerTest.particles.TileCrackParticle;

public enum ParticleType
{
	TILE_CRACK(0, TileCrackParticle.class);
	
	int particleId;
	Class<? extends IParticle> clazz;
	
	ParticleType(int id, Class<? extends IParticle> clazz)
	{
		this.particleId = id;
		this.clazz = clazz;
	}
	
	public int getParticleId()
	{
		return particleId;
	}
	
	public Class<? extends IParticle> getClazz()
	{
		return clazz;
	}
}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SSpawnParticle extends Packet<IClientHandler>
{

	int x;
	int y;
	int particleType;
	int count;
	long seed;
	
	public SSpawnParticle(int x, int y, int particleType, int count)
	{
		this.x = x;
		this.y = y;
		this.particleType = particleType;
		this.count = count;
		seed = Util.getRandomLong(Long.MAX_VALUE - count, Long.MIN_VALUE);
	}

	public SSpawnParticle()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(particleType);
		output.writeInt(count);
		output.writeLong(seed);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
		this.particleType = input.readInt();
		this.count = input.readInt();
		this.seed = input.readLong();
	}
	
	public int getParticleType()
	{
		return particleType;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public long getSeed()
	{
		return seed;
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSpawnParticlePacket(this);
	}

}

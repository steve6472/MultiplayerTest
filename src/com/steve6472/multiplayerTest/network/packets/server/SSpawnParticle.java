/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.World;
import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SSpawnParticle extends Packet<IClientHandler>
{

	int x;
	int y;
	int hitId;
	int count;
	int worldId;
	long seed;
	
	public SSpawnParticle(int x, int y, int hitId, int count, int worldId)
	{
		this.x = x;
		this.y = y;
		this.hitId = hitId;
		this.count = count;
		this.worldId = worldId;
		seed = Util.getRandomLong(Long.MAX_VALUE - count, Long.MIN_VALUE);
	}
	
	public SSpawnParticle(int x, int y, int hitId, int count, World world)
	{
		this(x, y, hitId, count, world.getWorldId());
	}

	public SSpawnParticle()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(hitId);
		output.writeInt(count);
		output.writeInt(worldId);
		output.writeLong(seed);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
		this.hitId = input.readInt();
		this.count = input.readInt();
		this.worldId = input.readInt();
		this.seed = input.readLong();
	}
	
	public int getHitId()
	{
		return hitId;
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
	
	public int getWorldId()
	{
		return worldId;
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleSpawnParticlePacket(this);
	}

}

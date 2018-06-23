/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.multiplayerTest.particles.TileCrackParticle;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SSpawnParticle extends SPacket
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
	
	public SSpawnParticle(int x, int y, int hitId, int count, ServerWorld world)
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
	public void handlePacket(Client client, ClientGui clientGui)
	{
		//Util.getRandomInt(3, 1, Util.getRandomLong(i, Long.MIN_VALUE, packet.getSeed()))
		
		for (int i = 0; i < getCount(); i++)
		{
			double ang = Util.getRandomDouble(360, 0, Util.getRandomLong(i, Long.MIN_VALUE, getSeed()));
			TileCrackParticle particle = new TileCrackParticle(
					x,
					y,
					ang,
//					Util.getRandomInt(20, 10, Util.getRandomLong(i, Long.MIN_VALUE, seed)),
					Util.getRandomInt(20, 10, Util.getRandomLong(i, Long.MIN_VALUE, seed)),
					hitId % ClientGui.atlas.getSize(),
					hitId / ClientGui.atlas.getSize(),
					Util.getRandomDouble(1.48d + 0.5d, 1.48d - 0.5d),
					(float) ang + Util.getRandomFloat(30, -30));
			clientGui.world.addParticles.add(particle);
		}
	}

}

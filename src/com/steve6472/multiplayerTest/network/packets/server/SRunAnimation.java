/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 29. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SRunAnimation extends Packet<IClientHandler>
{
	
	int animationId;
	DataStream data;
	
	public SRunAnimation(int animationId, DataStream data)
	{
		this.animationId = animationId;
		this.data = data;
	}
	
	public SRunAnimation()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(animationId);
		for (Object o : data.getData())
		{
			output.writeObject(o);
		}
	}

	@Override
	public void input(DataStream input)
	{
		this.animationId = input.readInt();
		data = new DataStream();
		for (Object o : input.getData())
		{
			this.data.writeObject(o);
		}
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleRunAnimation(this);
	}
	
	public int getAnimationId()
	{
		return animationId;
	}
	
	public DataStream getData()
	{
		return data;
	}

}

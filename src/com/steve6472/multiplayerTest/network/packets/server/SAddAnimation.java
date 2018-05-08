/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 5. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.gfx.animations.Animation;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SAddAnimation extends Packet<IClientHandler>
{
	
	Animation animation;
	
	public SAddAnimation(Animation animation)
	{
		this.animation = animation;
	}

	@Override
	public void output(DataStream output)
	{
		output.writeObject(animation);
	}

	@Override
	public void input(DataStream input)
	{
		animation = (Animation) input.readObject();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleAddAnimation(this);
	}
	
	public Animation getAnimation()
	{
		return animation;
	}

}

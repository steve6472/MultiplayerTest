/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.MouseHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CLeftRelease extends Packet<IServerHandler>
{

	int x, y;
	
	public CLeftRelease()
	{
	}
	
	public CLeftRelease(MouseHandler mh)
	{
		this.x = mh.getMouseX();
		this.y = mh.getMouseY();
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleLeftReleasePacket(this);
	}

}

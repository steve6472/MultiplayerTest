/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CMovePacket extends Packet<IServerHandler>
{

	public int x, y;
	
	public CMovePacket(int xa, int ya)
	{
		this.x = xa;
		this.y = ya;
	}
	
	public CMovePacket()
	{
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

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleMovePacket(this);
	}

}

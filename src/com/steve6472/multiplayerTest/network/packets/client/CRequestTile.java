/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 3. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CRequestTile extends Packet<IServerHandler>
{
	int index;
	
	public CRequestTile(int index)
	{
		this.index = index;
	}
	
	public CRequestTile()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(index);
	}

	@Override
	public void input(DataStream input)
	{
		this.index = input.readInt();
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleRequestTile(this);
	}

	public int getIndex()
	{
		return index;
	}

}

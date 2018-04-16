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

public class SRunEvent extends Packet<IClientHandler>
{
	
	int eventId;
	DataStream data;
	
	public SRunEvent(int eventId, DataStream data)
	{
		this.eventId = eventId;
		this.data = data;
	}
	
	public SRunEvent()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(eventId);
		for (Object o : data.getData())
		{
			output.writeObject(o);
		}
	}

	@Override
	public void input(DataStream input)
	{
		this.eventId = input.readInt();
		data = new DataStream();
		for (Object o : input.getData())
		{
			this.data.writeObject(o);
		}
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleRunEvent(this);
	}
	
	public int getEventId()
	{
		return eventId;
	}
	
	public DataStream getData()
	{
		return data;
	}

}

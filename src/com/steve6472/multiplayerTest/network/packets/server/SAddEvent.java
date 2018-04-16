/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 29. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.Event;
import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SAddEvent extends Packet<IClientHandler>
{
	Event event;
	
	public SAddEvent(Event event)
	{
		this.event = event;
	}
	
	public SAddEvent()
	{
		
	}

	@Override
	public void output(DataStream output)
	{
		output.writeObject(event);
	}

	@Override
	public void input(DataStream input)
	{
		this.event = (Event) input.readObject();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleAddEvent(this);
	}
	
	public Event getEvent()
	{
		return event;
	}

}

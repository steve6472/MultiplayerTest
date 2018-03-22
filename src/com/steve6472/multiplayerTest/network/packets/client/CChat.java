/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CChat extends Packet<IServerHandler>
{
	
	String text;
	
	public CChat(String text)
	{
		this.text = text;
	}
	
	public CChat()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeString(text);
	}

	@Override
	public void input(DataStream input)
	{
		text = input.readString();
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleChat(this);
	}
	
	public String getText()
	{
		return text;
	}

}

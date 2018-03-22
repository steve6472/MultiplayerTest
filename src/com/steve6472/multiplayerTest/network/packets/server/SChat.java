/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class SChat extends Packet<IClientHandler>
{
	
	String text;
	int networkId;
	
	public SChat(String text, int networkId)
	{
		this.text = text;
		this.networkId = networkId;
	}
	
	public SChat()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeString(text);
		output.writeInt(networkId);
	}

	@Override
	public void input(DataStream input)
	{
		text = input.readString();
		networkId = input.readInt();
	}

	@Override
	public void handlePacket(IClientHandler handler)
	{
		handler.handleChat(this);
	}
	
	public String getText()
	{
		return text;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

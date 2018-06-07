/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SSetName extends SPacket
{
	
	String name;
	int networkId;
	
	public SSetName(String name, int networkId)
	{
		this.name = name;
		this.networkId = networkId;
	}
	
	public SSetName()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(networkId);
		output.writeString(name);
	}

	@Override
	public void input(DataStream input)
	{
		this.networkId = input.readInt();
		this.name = input.readString();
	}
	
	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		PlayerMP player = client.getPlayer(getNetworkId());
		player.setName(getName());
	}

	public String getName()
	{
		return name;
	}
	
	public int getNetworkId()
	{
		return networkId;
	}

}

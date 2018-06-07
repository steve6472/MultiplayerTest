/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SSetNetworkId extends SPacket
{
	
	int networkId;
	
	public SSetNetworkId(int networkId)
	{
		this.networkId = networkId;
	}
	
	public SSetNetworkId()
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(networkId);
	}

	@Override
	public void input(DataStream input)
	{
		this.networkId = input.readInt();
	}

	public int getNetworkId()
	{
		return networkId;
	}
	
	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		System.out.println("Setting client's networkId to " + getNetworkId());
		client.networkId = getNetworkId();
	}
	
}

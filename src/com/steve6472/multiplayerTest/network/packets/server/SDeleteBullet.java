/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 26. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SDeleteBullet extends SPacket
{

	int networkId;
	
	public SDeleteBullet(int networkId)
	{
		this.networkId = networkId;
	}

	public SDeleteBullet()
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
		networkId = input.readInt();
	}
	
	public int getNetworkId()
	{
		return networkId;
	}
	
	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		int bulletIndex = client.getBulletIndex(getNetworkId());

		if (bulletIndex != -1)
		{
			clientGui.bullets.get(bulletIndex).setDead();
		}
	}

}

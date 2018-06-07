/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 24. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SPingResponse extends SPacket
{

	@Override
	public void output(DataStream output)
	{
	}

	@Override
	public void input(DataStream input)
	{
	}

	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		clientGui.getClientController().setPing(System.currentTimeMillis() - clientGui.getClientController().pingStart);
	}

}

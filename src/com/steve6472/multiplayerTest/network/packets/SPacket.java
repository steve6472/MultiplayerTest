/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 27. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.handlers.IClientHandler;
import com.steve6472.sge.main.networking.packet.Packet;

public abstract class SPacket extends Packet<IClientHandler>
{
	@Override
	public void handlePacket(IClientHandler handler)
	{
	}
	
	public abstract void handlePacket(Client client, ClientGui clientGui);
}

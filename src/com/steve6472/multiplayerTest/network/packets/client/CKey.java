/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.Server;
import com.steve6472.multiplayerTest.network.packets.CPacket;
import com.steve6472.sge.main.networking.packet.DataStream;

public class CKey extends CPacket
{
	
	int key;
	int action;
	int mods;

	public CKey(int key, int action, int mods)
	{
		this.key = key;
		this.action = action;
		this.mods = mods;
	}
	
	public CKey()
	{
	}

	@Override
	public void handlePacket(Server server, ServerGui serverGui)
	{
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(key);
		output.writeInt(action);
		output.writeInt(mods);
	}

	@Override
	public void input(DataStream input)
	{
		key = input.readInt();
		action = input.readInt();
		mods = input.readInt();
	}

}

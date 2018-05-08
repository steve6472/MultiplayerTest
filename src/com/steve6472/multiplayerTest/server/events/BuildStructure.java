/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 29. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.events;

import com.steve6472.multiplayerTest.ClientGui;
import com.steve6472.multiplayerTest.Event;
import com.steve6472.multiplayerTest.Game;
import com.steve6472.sge.main.networking.packet.DataStream;

public class BuildStructure extends Event
{

	private static final long serialVersionUID = 5180061260182072851L;

	@Override
	public void runEvent(ClientGui clientGui, DataStream data)
	{
		int x = data.readInt();
		int y = data.readInt();
		int type = data.readInt();
		Game.structures[type].generateStructure(x, y, clientGui.world);
	}

	@Override
	public int getId()
	{
		return 0;
	}
}

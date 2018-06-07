/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 30. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.events;

import com.steve6472.multiplayerTest.Event;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.sge.main.game.world.World;
import com.steve6472.sge.main.networking.packet.DataStream;

public class MoveChunks extends Event
{
	private static final long serialVersionUID = 3581210939088034609L;
	
	@Override
	public void runEvent(ClientGui clientGui, DataStream data)
	{
		int type = data.readInt();
		if (type == 0)
		{
			for (int i = 6; i > 0; i--)
			{
				move(i - 1, 0, i, 0, clientGui.world);
				move(i - 1, 1, i, 1, clientGui.world);
				move(i - 1, 2, i, 2, clientGui.world);
				move(i - 1, 3, i, 3, clientGui.world);
				move(i - 1, 4, i, 4, clientGui.world);
				move(i - 1, 5, i, 5, clientGui.world);
				move(i - 1, 6, i, 6, clientGui.world);
			}
		}
		if (type == 1)
		{
			for (int i = 0; i < 6; i++)
			{
				move(i + 1, 0, i, 0, clientGui.world);
				move(i + 1, 1, i, 1, clientGui.world);
				move(i + 1, 2, i, 2, clientGui.world);
				move(i + 1, 3, i, 3, clientGui.world);
				move(i + 1, 4, i, 4, clientGui.world);
				move(i + 1, 5, i, 5, clientGui.world);
				move(i + 1, 6, i, 6, clientGui.world);
			}
		}
		if (type == 2)
		{
			for (int i = 6; i > 0; i--)
			{
				move(0, i - 1, 0, i, clientGui.world);
				move(1, i - 1, 1, i, clientGui.world);
				move(2, i - 1, 2, i, clientGui.world);
				move(3, i - 1, 3, i, clientGui.world);
				move(4, i - 1, 4, i, clientGui.world);
				move(5, i - 1, 5, i, clientGui.world);
				move(6, i - 1, 6, i, clientGui.world);
			}
		}
		if (type == 3)
		{
			for (int i = 0; i < 6; i++)
			{
				move(0, i + 1, 0, i, clientGui.world);
				move(1, i + 1, 1, i, clientGui.world);
				move(2, i + 1, 2, i, clientGui.world);
				move(3, i + 1, 3, i, clientGui.world);
				move(4, i + 1, 4, i, clientGui.world);
				move(5, i + 1, 5, i, clientGui.world);
				move(6, i + 1, 6, i, clientGui.world);
			}
		}
	}
	
	private void move(int sx, int sy, int dx, int dy, World world)
	{
		world.setChunk(dx, dy, world.getChunk(sx, sy));
	}

	@Override
	public int getId()
	{
		return 1;
	}

}

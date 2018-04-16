/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 25. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.client;

import com.steve6472.multiplayerTest.network.handlers.IServerHandler;
import com.steve6472.sge.main.networking.packet.DataStream;
import com.steve6472.sge.main.networking.packet.Packet;

public class CMouseButton extends Packet<IServerHandler>
{

	int x, y;
	int button;
	/**
	 * 0 - Press
	 * 1 - Release
	 */
	int action;
	
	public CMouseButton()
	{
	}
	
	public CMouseButton(int x, int y, int button, int action)
	{
		this.x = x;
		this.y = y;
		this.button = button;
		this.action = action;
	}

	@Override
	public void output(DataStream output)
	{
		output.writeInt(x);
		output.writeInt(y);
		output.writeInt(button);
		output.writeInt(action);
	}

	@Override
	public void input(DataStream input)
	{
		this.x = input.readInt();
		this.y = input.readInt();
		this.button = input.readInt();
		this.action = input.readInt();
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getAction()
	{
		return action;
	}
	
	public int getButton()
	{
		return button;
	}

	@Override
	public void handlePacket(IServerHandler handler)
	{
		handler.handleMouseButtonPacket(this);
	}

}

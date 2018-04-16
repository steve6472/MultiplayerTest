/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 29. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.io.Serializable;

import com.steve6472.sge.main.networking.packet.DataStream;

public abstract class Event implements Serializable
{	
	private static final long serialVersionUID = -94118317183660372L;

	public abstract void runEvent(ClientGui clientGui, DataStream data);

	public abstract int getId();
}

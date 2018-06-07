/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 7. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.network.packets.server;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.GameInventory;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.network.Client;
import com.steve6472.multiplayerTest.network.packets.SPacket;
import com.steve6472.sge.main.game.inventory.Item;
import com.steve6472.sge.main.networking.packet.DataStream;

public class SOpenInventory extends SPacket
{
	int[] items;
	int width;
	int height;
	
	public SOpenInventory(int[] items, int width, int height)
	{
		this.items = items;
		this.width = width;
		this.height = height;
	}
	
	public SOpenInventory()
	{
	}
	
	@Override
	public void output(DataStream output)
	{
		output.writeIntArr(items);
		output.writeInt(width);
		output.writeInt(height);
	}

	@Override
	public void input(DataStream input)
	{
		items = input.readIntArr();
		width = input.readInt();
		height = input.readInt();
	}

	@Override
	public void handlePacket(Client client, ClientGui clientGui)
	{
		GameInventory inv = new GameInventory(null, getWidth(), getHeight(), Item.AIR);
		
		for (int i = 0; i < getItems().length; i++)
			inv.getSlot(i).setItemId(getItems()[i]);
			
		Game.inventoryRenderer.showInventory(inv);
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int[] getItems()
	{
		return items;
	}
	
	public int getWidth()
	{
		return width;
	}

}

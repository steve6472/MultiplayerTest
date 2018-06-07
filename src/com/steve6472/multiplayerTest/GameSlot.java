/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 12. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.main.game.inventory.ItemSlot;

public class GameSlot extends ItemSlot
{
	private static final long serialVersionUID = 3073679038071328731L;
	
	int itemId;

	public GameSlot(String name, int id)
	{
		super(name, id);
	}
	
	public int getItemId()
	{
		return itemId;
	}
	
	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}

}

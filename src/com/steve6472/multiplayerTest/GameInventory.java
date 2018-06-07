/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.main.game.BaseEntity;
import com.steve6472.sge.main.game.inventory.Inventory;
import com.steve6472.sge.main.game.inventory.Item;

public class GameInventory extends Inventory<GameSlot>
{
	int width;
	int height;

	public GameInventory(BaseEntity entity, int width, int height, Item nullItem, String... slotNames)
	{
		super(entity, GameSlot.class, nullItem, slotNames);
		this.width = width;
		this.height = height;
	}

	public GameInventory(BaseEntity entity, int width, int height, Item nullItem)
	{
		super(entity, GameSlot.class, nullItem, width * height);
		this.width = width;
		this.height = height;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int getWidth()
	{
		return width;
	}

}

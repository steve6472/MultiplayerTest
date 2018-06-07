/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 12. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.items;

import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;

public class PlaceableItem extends BaseItem
{
	private static final long serialVersionUID = 7364357674504413819L;
	
	int place;

	public PlaceableItem(int id, String sprite, String name, String[] lore, int place)
	{
		super(id, sprite, name, lore);
		this.place = place;
	}

	public PlaceableItem(int id, String sprite, String name, String[] lore, ServerTile place)
	{
		super(id, sprite, name, lore);
		this.place = place.getId();
	}
	
	@Override
	public void mouseEvent(int x, int y, int tx, int ty, PlayerMP player, int action, int button, GameWorld world, int slot)
	{
		//Remove one item from slot
//		Inventory inv = player.getInventory();
		
		world.setTileInWorldSafe(tx, ty, 0, place);
	}
}

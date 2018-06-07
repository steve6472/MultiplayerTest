/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 12. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.items;

import com.steve6472.multiplayerTest.GameWorld;
import com.steve6472.multiplayerTest.PlayerMP;

public class BaseItem extends ServerItem
{
	private static final long serialVersionUID = -5577925873909114679L;

	public BaseItem(int id, String sprite, String name, String[] lore)
	{
		super(id, sprite, name, lore);
	}

	@Override
	public void mouseEvent(int x, int y, int tx, int ty, PlayerMP player, int action, int button, GameWorld world, int slot)
	{
	}

}

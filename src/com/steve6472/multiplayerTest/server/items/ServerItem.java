/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 12. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.server.items;

import java.io.File;

import com.steve6472.multiplayerTest.PlayerMP;
import com.steve6472.multiplayerTest.server.ServerWorld;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.game.Atlas;
import com.steve6472.sge.main.game.inventory.Item;
import com.steve6472.sge.main.game.world.GameTile;

public abstract class ServerItem extends Item
{
	private static final long serialVersionUID = 8598337660992730645L;
	private final int id;
	private final String sprite;
	private final String name;
	private final String[] lore;
	
	private int indexX = 0;
	private int indexY = 0;
	
	private static Atlas atlas;
	private static SGArray<String> sprites = new SGArray<String>();
	private static SGArray<ServerItem> items = new SGArray<ServerItem>();

	public ServerItem(int id, String sprite, String name, String[] lore)
	{
		this.id = id;
		this.sprite = sprite == null || sprite.isEmpty() ? null :"items\\" + sprite;
		this.name = name;
		this.lore = lore == null ? new String[0] : lore;
	}
	
	public static ServerItem air;
	public static ServerItem wallBlueprint;
	
	public static void initItems()
	{
		air = new BaseItem(0, null, "", null).init();
		wallBlueprint = new PlaceableItem(0, "wallBlueprint.png", "Wall Blueprint", null, ServerTile.wallBlueprint).init();

		atlas = new Atlas(sprites.toList());
		atlas.create(32, (x, y, i) -> items.getObject(i).setIndexes(x, y, i));
		GameTile.initGameTiles(getAtlas(), 32, 32);
		System.out.println("Items initialized");
		atlas.getAtlas().save(new File("serverItems.png"));
	}
	
	public ServerItem init()
	{
		sprites.addObject(sprite);
		items.addObject(this);
		return this;
	}
	
	public void setIndexes(int x, int y, int index)
	{
		items.getObject(index).indexX = x;
		items.getObject(index).indexY = y;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String[] getLore()
	{
		return lore;
	}
	
	public int getIndexX()
	{
		return indexX;
	}
	
	public int getIndexY()
	{
		return indexY;
	}
	
	public static Atlas getAtlas()
	{
		return atlas;
	}
	
	public static SGArray<ServerItem> getItems()
	{
		return items;
	}
	
	public abstract void mouseEvent(int x, int y, int tx, int ty, PlayerMP player, int action, int button, ServerWorld world, int slot);
}

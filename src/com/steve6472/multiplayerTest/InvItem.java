/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 5. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.main.game.inventory.Item;

public class InvItem extends Item
{
	private static final long serialVersionUID = -1093810041774376924L;
	
	int index;
	String name;
	String[] lore;
	float r, g, b, a;
	
	public InvItem(int index, String name, String[] lore, float r, float g, float b, float a)
	{
		this.index = index;
		this.name = name;
		this.lore = lore;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public InvItem(int index, String name, String[] lore)
	{
		this.index = index;
		this.name = name;
		this.lore = lore;
	}
	
	public InvItem(int index, String name)
	{
		this.index = index;
		this.name = name;
		this.lore = new String[0];
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setLore(String[] lore)
	{
		this.lore = lore;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String[] getLore()
	{
		return lore;
	}
	
	public float[] getColors()
	{
		return new float[] {r, g, b, a};
	}
}

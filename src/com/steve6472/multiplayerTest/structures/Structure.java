/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

import com.steve6472.multiplayerTest.World;

public abstract class Structure
{
	public abstract int[] getStructure();
	
	public abstract int getStructureWidth();
	public abstract int getStructureHeight();
	
	public void generateStructure(int x, int y, World world)
	{
		for (int i = 0; i < getStructureWidth(); i++)
		{
			for (int j = 0; j < getStructureHeight(); j++)
			{
				setTile(x + i, y + j, getStructure()[i + j * getStructureWidth()], world);
			}
		}
	}
	
	private void setTile(int x, int y, int id, World world)
	{
		if (x < 0 || y < 0 || x >= world.getTilesX() || y >= world.getTilesY())
			return;
		world.setTile(id, x, y, false);
	}

}

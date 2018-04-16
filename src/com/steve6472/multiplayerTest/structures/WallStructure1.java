/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

public class WallStructure1 extends Structure
{

	public WallStructure1()
	{
	}

	@Override
	public int[] getStructure()
	{
		return new int[] {2, 1, 9,
						  6, 5, 6,
						  6, 6, 6,
						  3, 1, 9};
	}

	@Override
	public int getStructureWidth()
	{
		return 3;
	}

	@Override
	public int getStructureHeight()
	{
		return 4;
	}

}

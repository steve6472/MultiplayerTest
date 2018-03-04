/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

public class WallStructure0 extends Structure
{

	public WallStructure0()
	{
	}

	@Override
	public int[] getStructure()
	{
		return new int[] {9, 1, 3, 5, 2, 9, 4,
						  5, 2, 5, 9, 1, 3, 9};
	}

	@Override
	public int getStructureWidth()
	{
		return 7;
	}

	@Override
	public int getStructureHeight()
	{
		return 2;
	}

}

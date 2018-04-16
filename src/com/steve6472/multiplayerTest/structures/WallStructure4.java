/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

public class WallStructure4 extends Structure
{

	public WallStructure4()
	{
	}

	@Override
	public int[] getStructure()
	{
		return new int[] {6, 9, 9, 9, 6,
						  9, 9, 6, 9, 9,
						  9, 6, 6, 6, 9,
						  9, 9, 6, 9, 9,
						  6, 9, 9, 9, 6};
	}

	@Override
	public int getStructureWidth()
	{
		return 5;
	}

	@Override
	public int getStructureHeight()
	{
		return 5;
	}

}

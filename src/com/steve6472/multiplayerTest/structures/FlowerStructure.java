/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

public class FlowerStructure extends Structure
{

	public FlowerStructure()
	{
	}

	@Override
	public int[] getStructure()
	{
		return new int[] {7, 7, 7,
						  7, 6, 7,
						  7, 6, 6,
						  7, 6, 6};
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

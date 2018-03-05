/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

public class Lake extends Structure
{

	public Lake()
	{
	}

	@Override
	public int[] getStructure()
	{
		return new int[] {7, 6, 6, 6, 7, 6, 6, 6,
						  6, 6, 8, 8, 7, 8, 6, 7,
						  6, 8, 8, 10, 10, 8, 6, 7,
						  6, 8, 10, 10, 10, 10, 8, 6,
						  6, 8, 10, 10, 10, 10, 8, 6,
						  6, 8, 8, 10, 10, 10, 8, 6,
						  7, 6, 8, 8, 10, 8, 8, 6,
						  6, 6, 6, 7, 6, 6, 7, 6,};
	}

	@Override
	public int getStructureWidth()
	{
		return 8;
	}

	@Override
	public int getStructureHeight()
	{
		return 8;
	}

}

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

public class WallStructure2 extends Structure
{

	public WallStructure2()
	{
	}

	@Override
	public int[] getStructure()
	{
		return new int[] {9, 9, 9,
						  9, 6, 9,
						  9, 9, 9,};
	}

	@Override
	public int getStructureWidth()
	{
		return 3;
	}

	@Override
	public int getStructureHeight()
	{
		return 3;
	}

}

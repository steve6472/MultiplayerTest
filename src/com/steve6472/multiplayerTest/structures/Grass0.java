/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 4. 3. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.structures;

public class Grass0 extends Structure
{

	public Grass0()
	{
	}

	@Override
	public int[] getStructure()
	{
		return new int[] {6, 6,
						  6, 6};
	}

	@Override
	public int getStructureWidth()
	{
		return 2;
	}

	@Override
	public int getStructureHeight()
	{
		return 2;
	}

}

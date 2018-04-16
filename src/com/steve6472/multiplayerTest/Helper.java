/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 14. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import org.joml.Matrix4f;

import com.steve6472.sge.main.SArray;

public class Helper
{
	static HelperDataLayer lowLayer;
	static HelperDataLayer currentLayer;
	static SArray savedLayers;
	private static boolean initialised = false;
	
	public static void initHelper()
	{
		currentLayer = new HelperDataLayer();
		savedLayers = new SArray(0, false, false);
		currentLayer.color(0, 0, 0, 0);
		initialised = true;
	}
	
	public static boolean isInitialised()
	{
		return initialised;
	}
	
	public static void pushLayer()
	{
		lowLayer = currentLayer.clone();
		currentLayer.actions = 0;
	}
	
	public static void popLayer()
	{
		currentLayer = lowLayer;
		currentLayer.actions = 0;
		lowLayer = null;
	}
	
	public static void saveLayer(int saveIndex)
	{
		savedLayers.setObject(saveIndex, currentLayer);
	}
	
	public static void loadLayer(int layerIndex)
	{
		currentLayer = (HelperDataLayer) savedLayers.getObject(layerIndex);
	}
	
	public static void translate(float x, float y, float z)
	{
		currentLayer.translate(x, y, z);
		currentLayer.scale(0, 0, 0);
		currentLayer.rotate(0, 0, 0, 0);
		currentLayer.actions++;
		currentLayer.acts.addObject(0);
	}
	
	public static void scale(float x, float y, float z)
	{
		currentLayer.translate(0, 0, 0);
		currentLayer.scale(x, y, z);
		currentLayer.rotate(0, 0, 0, 0);
		currentLayer.actions++;
		currentLayer.acts.addObject(1);
	}
	
	public static void scale(float xyz)
	{
		scale(xyz, xyz, xyz);
	}
	
	/**
	 * 
	 * @param ang in Degrees
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void rotate(float ang, float x, float y, float z)
	{
		currentLayer.translate(0, 0, 0);
		currentLayer.scale(0, 0, 0);
		currentLayer.rotate((float) Math.toRadians(ang), x, y, z);
		currentLayer.actions++;
		currentLayer.acts.addObject(2);
	}
	
	public static void color(float red, float green, float blue, float alpha)
	{
		currentLayer.color(red, green, blue, alpha);
	}
	
	public static float getRed() 	{ return currentLayer.color.getObject(0); }
	public static float getGreen() 	{ return currentLayer.color.getObject(1); }
	public static float getBlue() 	{ return currentLayer.color.getObject(2); }
	public static float getAlpha() 	{ return currentLayer.color.getObject(3); }
	
	public static Matrix4f toMatrix()
	{
		Matrix4f mat = new Matrix4f();
		for (int i = 0; i < currentLayer.actions; i++)
		{
			switch (currentLayer.acts.getObject(i))
			{
			case 0:
				mat.translate(currentLayer.xyz.getObject(i * 3 + 0), currentLayer.xyz.getObject(i * 3 + 1), currentLayer.xyz.getObject(i * 3 + 2));
				break;
			case 1:
				mat.scale(currentLayer.scalexyz.getObject(i * 3 + 0), currentLayer.scalexyz.getObject(i * 3 + 1), currentLayer.scalexyz.getObject(i * 3 + 2));
				break;
			case 2:
				mat.rotate(currentLayer.rotxyz.getObject(i * 4 + 0), currentLayer.rotxyz.getObject(i * 4 + 1), currentLayer.rotxyz.getObject(i * 4 + 2), currentLayer.rotxyz.getObject(i * 4 + 3));
				break;
			}
		}
		return mat;
	}
}

package com.steve6472.multiplayerTest;

import com.steve6472.sge.main.SGArray;

public class HelperDataLayer
{
	protected SGArray<Float> xyz;
	protected SGArray<Float> scalexyz;
	protected SGArray<Float> rotxyz;
	protected SGArray<Integer> acts;
	protected SGArray<Float> color;
	
	int actions;
	
	public HelperDataLayer()
	{
		xyz = 		new SGArray<Float>(0, true, false);
		scalexyz = 	new SGArray<Float>(0, true, false);
		rotxyz = 	new SGArray<Float>(0, true, false);
		acts = 		new SGArray<Integer>(0, true, false);
		color = 	new SGArray<Float>(4, false, false);
		color(0, 0, 0, 0);
	}
	
	public void translate(float x, float y, float z)
	{
		xyz.addObject(x);
		xyz.addObject(y);
		xyz.addObject(z);
	}
	
	public void scale(float x, float y, float z)
	{
		scalexyz.addObject(x);
		scalexyz.addObject(y);
		scalexyz.addObject(z);
	}
	
	public void rotate(float ang, float x, float y, float z)
	{
		rotxyz.addObject(ang);
		rotxyz.addObject(x);
		rotxyz.addObject(y);
		rotxyz.addObject(z);
	}
	
	public void color(float red, float green, float blue, float alpha)
	{
		color.setObject(0, red);
		color.setObject(1, green);
		color.setObject(2, blue);
		color.setObject(3, alpha);
	}
	
	public HelperDataLayer clone()
	{
		HelperDataLayer h = new HelperDataLayer();

		h.xyz = xyz.copy();
		h.scalexyz = scalexyz.copy();
		h.rotxyz = rotxyz.copy();
		
		return h;
	}
	
	public int getActions()
	{
		return actions;
	}
	
	public SGArray<Integer> getActs()
	{
		return acts;
	}
	
	public SGArray<Float> getColor()
	{
		return color;
	}
	
	public SGArray<Float> getRotxyz()
	{
		return rotxyz;
	}
	
	public SGArray<Float> getScalexyz()
	{
		return scalexyz;
	}
	
	public SGArray<Float> getXyz()
	{
		return xyz;
	}
}
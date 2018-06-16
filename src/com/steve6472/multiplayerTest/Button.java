/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.gfx.Camera;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.SmallNineSlice;
import com.steve6472.sge.gui.GuiUtils;
import com.steve6472.sge.main.MainApplication;

public class Button
{
	float x;
	float y;
	
	float size;
	float width;
	float height;
	
	SmallNineSlice but, hov;
	MainApplication ma;
	
	public Button(float x, float y, float width, float height, float size, MainApplication ma, Shader shader, Camera camera)
	{
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		this.size = size;
		
		this.ma = ma;
		
		but = new SmallNineSlice(0, 0, 6, 6, MainApplication.sprites, shader, camera);
		but.setCorner(0, 0, 2, 2);
		but.setTopEdge(2, 0, 2, 2, 2);
		but.setSideEdge(0, 2, 2, 2, 2);
		but.createMiddle();
		but.setScaleMultiplier(size, size);
		but.setScale(width, height, 1);

		
		hov = new SmallNineSlice(12, 0, 6, 6, MainApplication.sprites, shader, camera);
		hov.setCorner(0, 0, 2, 2);
		hov.setTopEdge(2, 0, 2, 2, 2);
		hov.setSideEdge(0, 2, 2, 2, 2);
		hov.createMiddle();
		hov.setScaleMultiplier(size, size);
		hov.setScale(width, height, 1);
		
	}
	
	public void update(float x, float y, float width, float height, float size)
	{
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		this.size = size;
		
		but.setScaleMultiplier(size, size);
		but.setScale(width / size, height / size, 1);
		
		hov.setScaleMultiplier(size, size);
		hov.setScale(width / size, height / size, 1);
	}
	
	boolean hovered = false;
	
	public void render()
	{
		if (hovered)
			hov.render(ma.getCurrentWidth() / 2f - x - width / size * size - 3 * size, ma.getCurrentHeight() / 2f - y - height / size * size - 3 * size);
		else
			but.render(ma.getCurrentWidth() / 2f - x - width / size * size - 3 * size, ma.getCurrentHeight() / 2f - y - height / size * size - 3 * size);
	}
	
	public void tick()
	{
		hovered = GuiUtils.isCursorInRectangle(ma.getMouseHandler(), (int) x, (int) y, (int) (width * 2 + 6 * size), (int) (height * 2 + 6 * size));
	}
}

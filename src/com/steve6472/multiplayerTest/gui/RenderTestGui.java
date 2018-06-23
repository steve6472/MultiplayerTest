/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.gui;

import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.test.ShaderTest2;

public class RenderTestGui extends Gui implements KeyList
{

	private static final long serialVersionUID = -7181572382358516523L;
	
	public RenderTestGui(MainApplication mainApp)
	{
		super(mainApp);
	}
	
	Shader shader;
	Model model;
	Sprite tex0, tex1, mask;
	
	@Override
	public void showEvent()
	{
		getMainApp().getWindow().setSize(512, 512);
	}
	

	@Override
	public void createGui()
	{
		shader = new Shader("shaders\\mask");
		model = new Model(ShaderTest2.fillScreen(), ShaderTest2.createBasicTexture(), ShaderTest2.createArray(0f));
		tex0 = new Sprite("*grass.png");
		tex1 = new Sprite("*sand.png");
		mask = new Sprite("*mask.png");
	}
	
	byte byt = 0;
	
	@Override
	public void guiTick()
	{
		getMainApp().getWindow().setWindowText("" + getMainApp().getCurrentWidth() + "/" + getMainApp().getCurrentHeight());
		byt++;
		if (byt < 10)
		{
			getMainApp().getWindow().setSize(512, 512);
			getMainApp().getWindow().centerWindow();
		} else
		{
			byt = 11;
		}
	}
	
	float time = 0;
	
	@Override
	public void render(Screen screen)
	{
		tex0.bind(0);
		tex1.bind(1);
		mask.bind(2);
		
		shader.bind();
		shader.setUniform1i("tex0", 0);
		shader.setUniform1i("tex1", 1);
		shader.setUniform1i("mask", 2);

		shader.setUniform1f("x", time);
		shader.setUniform1f("y", time);
		
		time += 0.01f;
		
		model.render();
	}

}

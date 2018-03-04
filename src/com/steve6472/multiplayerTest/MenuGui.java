/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.GuiUtils;
import com.steve6472.sge.gui.components.Background;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.gui.components.ButtonEvents;
import com.steve6472.sge.main.MainApplication;

public class MenuGui extends Gui
{
	private static final long serialVersionUID = -5566643996662913418L;

	public MenuGui(MainApplication mainApp)
	{
		super(mainApp);
		showGui();
		switchRender();
	}

	@Override
	public void createGui()
	{
		GuiUtils.createBasicLayout(this);
		
		Button runServer = new Button("Run Server");
		runServer.setLocation(10, 35);
		runServer.setSize(100, 30);
		runServer.addEvent(new ButtonEvents()
		{
			@Override
			public void click()
			{
				((MultiplayerTest) getMainApp()).serverGui.showGui();
				hideGui();
			}
		});
		addComponent(runServer);
		
		Button runClient = new Button("Run Client");
		runClient.setLocation(120, 35);
		runClient.setSize(100, 30);
		runClient.addEvent(new ButtonEvents()
		{
			@Override
			public void click()
			{
				((MultiplayerTest) getMainApp()).clientGui.showGui();
				hideGui();
			}
		});
		addComponent(runClient);
	}

	@Override
	public void guiTick()
	{
	}

	@Override
	public void render(Screen screen)
	{
		Background.renderFrame(screen, getMainApp());
	}

}

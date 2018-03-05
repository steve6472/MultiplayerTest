/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.GuiUtils;
import com.steve6472.sge.gui.components.Background;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.gui.components.ButtonEvents;
import com.steve6472.sge.gui.components.TextField;
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
	
	TextField name;
	static TextField ip;
	static TextField port;

	@Override
	public void createGui()
	{
		GuiUtils.createBasicLayout(this);
		
		name = new TextField();
		name.setSize(210, 30);
		name.setLocation(14, 70);
		name.setText("unnamed");
		addComponent(name);
		
		ip = new TextField();
		ip.setSize(210, 30);
		ip.setLocation(14, 70 + 40);
		try
		{
			ip.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		addComponent(ip);
		
		port = new TextField();
		port.setSize(210, 30);
		port.setText("1337");
		port.setLocation(14, 70 + 80);
		addComponent(port);
		
		Button runServer = new Button("Run Server");
		runServer.setLocation(14, 35);
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
		runClient.setLocation(124, 35);
		runClient.setSize(100, 30);
		runClient.addEvent(new ButtonEvents()
		{
			@Override
			public void click()
			{
				ClientGui.name = name.getText();
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

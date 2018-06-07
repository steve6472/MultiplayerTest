/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.gui;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.server.items.ServerItem;
import com.steve6472.multiplayerTest.server.tiles.ServerTile;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.gui.GuiUtils;
import com.steve6472.sge.gui.components.Background;
import com.steve6472.sge.gui.components.Button;
import com.steve6472.sge.gui.components.ButtonEvents;
import com.steve6472.sge.gui.components.TextField;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.world.Chunk;

import com.steve6472.sge.main.game.world.World;

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
	public static TextField ip;
	public static TextField port;

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
				ServerTile.initTiles();
				ServerItem.initItems();
//				GameTile.initGameTiles(Tile.atlas, 32, 32, new Shader("shaders\\basev2"), 31, 17);
				Chunk.initChunks(8, 8, 1);
				World.initWorlds(64, 64);
				((Game) getMainApp()).serverGui.showGui();
				hideGui();
				getMainApp().resetOrtho();
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
//				GameTile.initGameTiles(Tile.atlas, 32, 32, new Shader("shaders\\basev2"), 31, 17);
//				Chunk.initChunks(16, 16, 1);
//				World.initWorlds(2, 2);
				ClientGui.name = name.getText();
				((Game) getMainApp()).clientGui.showGui();
				hideGui();
				getMainApp().resetOrtho();
			}
		});
		addComponent(runClient);
		
		Button runBoth = new Button("Run Both");
		runBoth.setLocation(124 * 3, 35);
		runBoth.setSize(100, 30);
		runBoth.addEvent(new ButtonEvents()
		{
			@Override
			public void click()
			{
				ServerTile.initTiles();
				ServerItem.initItems();
				Chunk.initChunks(8, 8, 1);
				World.initWorlds(64, 64);
				((Game) getMainApp()).serverGui.showGui();
				((Game) getMainApp()).serverGui.render = false;
				
				ClientGui.name = name.getText();
				((Game) getMainApp()).clientGui.showGui();
				hideGui();
				getMainApp().resetOrtho();
			}
		});
		addComponent(runBoth);
		
		Button runRenderTest = new Button("Render Test");
		runRenderTest.setLocation(124 + 110, 35);
		runRenderTest.setSize(100, 30);
		runRenderTest.addEvent(new ButtonEvents()
		{
			@Override
			public void click()
			{
				((Game) getMainApp()).renderTestGui.showGui();
				hideGui();
//				getMainApp().perspectiveGL(85f, (float) getMainApp().getCurrentWidth() / (float) getMainApp().getCurrentHeight(), 0.1f, 1000f);
//				getMainApp().resetOrtho();
			}
		});
		addComponent(runRenderTest);
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

/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.steve6472.multiplayerTest.network.packets.client.CChat;
import com.steve6472.multiplayerTest.network.packets.client.CLeftPress;
import com.steve6472.multiplayerTest.network.packets.client.CLeftRelease;
import com.steve6472.multiplayerTest.network.packets.client.CMovePacket;
import com.steve6472.multiplayerTest.network.packets.client.CRequestTile;
import com.steve6472.multiplayerTest.network.packets.client.CSetName;
import com.steve6472.multiplayerTest.network.packets.server.SChangeTile;
import com.steve6472.multiplayerTest.network.packets.server.SChat;
import com.steve6472.multiplayerTest.network.packets.server.SConnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SDeleteBullet;
import com.steve6472.multiplayerTest.network.packets.server.SDisconnectPlayer;
import com.steve6472.multiplayerTest.network.packets.server.SSetName;
import com.steve6472.multiplayerTest.network.packets.server.SSetNetworkId;
import com.steve6472.multiplayerTest.network.packets.server.SSetScore;
import com.steve6472.multiplayerTest.network.packets.server.SSetWorld;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnBullet;
import com.steve6472.multiplayerTest.network.packets.server.SSpawnParticle;
import com.steve6472.multiplayerTest.network.packets.server.STeleportPlayer;
import com.steve6472.sge.gfx.Font;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Texture;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.game.EntityList;
import com.steve6472.sge.main.networking.packet.DisconnectPacket;
import com.steve6472.sge.main.networking.packet.Packet;
import com.steve6472.sge.test.Camera;
import com.steve6472.sge.test.ShaderTest2;

public class MultiplayerTest extends MainApplication
{
	public ServerGui serverGui;
	public ClientGui clientGui;
	
	public static EntityList entityList;
	public static Camera camera;
	
	public static Model model;
	public static Shader shader;
	
	public static Shader fontShader;
	public static Model fontModel;
	
	public static Shader tileShade;
	public static Shader rectShader;
	
	public static Model shadeModelLine0, shadeModelLine1, shadeModelLine2, shadeModelLine3;
	public static Model shadeModelCornerIn0, shadeModelCornerIn5, shadeModelCornerIn2, shadeModelCornerIn4;
	public static Model shadeModelCornerOut0, shadeModelCornerOut5, shadeModelCornerOut2, shadeModelCornerOut4;
	
	@Override
	public void init()
	{
		entityList = new EntityList(this);
		
		entityList.addEntity(Bullet.class);
		
		Texture t = new Texture(new int[] {0}, 1, 1);
		
		Packet.addPacket(2, 	SConnectPlayer.class);
		Packet.addPacket(3, 	SDisconnectPlayer.class);
		Packet.addPacket(4, 	SDeleteBullet.class);
		Packet.addPacket(5, 	SSpawnBullet.class);
		Packet.addPacket(6, 	SSpawnParticle.class);
		Packet.addPacket(7, 	STeleportPlayer.class);
		Packet.addPacket(8, 	CLeftPress.class);
		Packet.addPacket(9, 	CLeftRelease.class);
		Packet.addPacket(10, 	CMovePacket.class);
		Packet.addPacket(11, 	SSetWorld.class);
		Packet.addPacket(12, 	SChangeTile.class);
		Packet.addPacket(13, 	CRequestTile.class);
		Packet.addPacket(14, 	SSetNetworkId.class);
		Packet.addPacket(15, 	SSetScore.class);
		Packet.addPacket(16, 	SSetName.class);
		Packet.addPacket(17, 	CSetName.class);
		Packet.addPacket(18, 	SChat.class);
		Packet.addPacket(19, 	CChat.class);
		
		camera = new Camera();
		camera.setSize(getWidth(), getHeight());

		serverGui = new ServerGui(this);
		clientGui = new ClientGui(this);

		fontShader = new Shader("shaders\\basev2");
		fontModel = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(8, 8, getFont().getFont()), ShaderTest2.createArray(0));

		shader = new Shader("shaders\\basev2");
		model = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(32, 32, Tile.atlas.getAtlas()), ShaderTest2.createArray(0));
		
		shadeModelLine0 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(1, 1, t), createLine());
		shadeModelLine1 = new Model(ShaderTest2.fillScreen1(), ShaderTest2.createTexture(1, 1, t), createLine());
		shadeModelLine2 = new Model(ShaderTest2.fillScreen2(), ShaderTest2.createTexture(1, 1, t), createLine());
		shadeModelLine3 = new Model(ShaderTest2.fillScreen3(), ShaderTest2.createTexture(1, 1, t), createLine());
		
		shadeModelCornerIn0 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(1, 1, t), createCornerIn());
		shadeModelCornerIn5 = new Model(ShaderTest2.fillScreen5(), ShaderTest2.createTexture(1, 1, t), createCornerIn());
		shadeModelCornerIn2 = new Model(ShaderTest2.fillScreen2(), ShaderTest2.createTexture(1, 1, t), createCornerIn());
		shadeModelCornerIn4 = new Model(ShaderTest2.fillScreen4(), ShaderTest2.createTexture(1, 1, t), createCornerIn());
		
		shadeModelCornerOut0 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(1, 1, t), createCornerOut());
		shadeModelCornerOut5 = new Model(ShaderTest2.fillScreen5(), ShaderTest2.createTexture(1, 1, t), createCornerOut());
		shadeModelCornerOut2 = new Model(ShaderTest2.fillScreen2(), ShaderTest2.createTexture(1, 1, t), createCornerOut());
		shadeModelCornerOut4 = new Model(ShaderTest2.fillScreen4(), ShaderTest2.createTexture(1, 1, t), createCornerOut());
		
		tileShade = new Shader("shaders\\shade");
		rectShader = new Shader("shaders\\rect");
		
		new MenuGui(this);
	}
	
	public float[] createLine()
	{
		return ShaderTest2.createArray(
				new Vector4f(0.0f, 0.0f, 0.0f, 0.5f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.5f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.0f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.0f)
				);
	}
	
	public float[] createCornerIn()
	{
		return ShaderTest2.createArray(
				new Vector4f(0.0f, 0.0f, 0.0f, 0.5f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.5f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.0f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.5f)
				);
	}
	
	public float[] createCornerOut()
	{
		return ShaderTest2.createArray(
				new Vector4f(0.0f, 0.0f, 0.0f, 0.0f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.0f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.5f),
				new Vector4f(0.0f, 0.0f, 0.0f, 0.0f)
				);
	}
	
	public static void drawFont(MainApplication mainApp, String text, int x, int y)
	{
		float size = 1 * 4;
		
		Matrix4f pro = new Matrix4f()
				.scale(size)
				.translate(size * 32 - 1 - (float) x / size, size * 18 - 1 - (float) y / size, 0);
			
		mainApp.getFont().getFont().bind();
		fontShader.bind();
		fontShader.setUniform1f("sampler", 0);

		for (int i = 0; i < text.length(); i++)
		{
			int char_index = Font.chars.indexOf(text.charAt(i));
			if (char_index >= 0)
			{
				renderChar(-i * 2, 0, char_index, (int) size, pro, camera);
			}
		}
	}
	
	private static void renderChar(float x, float y, int index, int size, Matrix4f mat, Camera camera)
	{
		size = 1;
		float indexX = (index % 64) / 64f;
		float indexY = (index / 64) / 64f;
		
		Matrix4f tilePos = new Matrix4f().translate(x, y, 0);
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(mat, target);
		target.mul(tilePos);
		
		fontShader.setUniform2f("texture", indexX, indexY);
		
		fontShader.setUniformMat4f("projection", target);
//		fontShader.setUniform4f("col", 0.21f, 0.21f, 0.21f, 1f);
		
		fontModel.render();
	}
	
	public static void drawSquare(float x, float y, float w, float h, int color)
	{
		Matrix4f pro = new Matrix4f()
				.scale(w / 2f, h / 2f, 0)
				.translate(x * -2 / w, y * -2 / h, 0);

		float width = (float) camera.getWidth();
		float height = (float) camera.getHeight();
		
		Matrix4f startPos = new Matrix4f().translate(width / w - 1, height / h - 1, 0);
		Matrix4f target = new Matrix4f();
		
		camera.getProjection().mul(pro, target);
		target.mul(startPos);
		
		float[] colors = Screen.getColors(color);
		
		rectShader.bind();
		rectShader.setUniform4f("color", colors[0], colors[1], colors[2], colors[3]);
		rectShader.setUniformMat4f("projection", target);
		
		model.render();
	}

	@Override
	public void tick()
	{
		tickGui();
	}

	@Override
	public void render(Screen screen)
	{
		renderGui();
	}

	public void exit()
	{
		if (clientGui.client != null)
			clientGui.client.sendPacket(new DisconnectPacket());
		glfwSetWindowShouldClose(window, true);
	}

	@Override
	public int getWidth()
	{
		return 16 * 32 * 2;
	}

	@Override
	public int getHeight()
	{
		return 9 * 32 * 2;
	}

	@Override
	public String getTitle()
	{
		return null;
	}

	@Override
	public void setWindowHints()
	{
//		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
	}
	
	public static void main(String[] args)
	{
		new MultiplayerTest();
	}

}

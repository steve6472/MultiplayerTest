/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 11. 2. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.multiplayerTest.gui.InventoryRenderer;
import com.steve6472.multiplayerTest.gui.MenuGui;
import com.steve6472.multiplayerTest.gui.RenderTestGui;
import com.steve6472.multiplayerTest.gui.ServerGui;
import com.steve6472.multiplayerTest.network.packets.client.*;
import com.steve6472.multiplayerTest.network.packets.client.inv.CCloseInventory;
import com.steve6472.multiplayerTest.network.packets.client.inv.CMoveItem;
import com.steve6472.multiplayerTest.network.packets.server.*;
import com.steve6472.multiplayerTest.network.packets.server.world.*;
import com.steve6472.multiplayerTest.structures.FlowerStructure;
import com.steve6472.multiplayerTest.structures.Grass0;
import com.steve6472.multiplayerTest.structures.Grass1;
import com.steve6472.multiplayerTest.structures.Lake;
import com.steve6472.multiplayerTest.structures.Structure;
import com.steve6472.multiplayerTest.structures.WallStructure0;
import com.steve6472.multiplayerTest.structures.WallStructure1;
import com.steve6472.multiplayerTest.structures.WallStructure2;
import com.steve6472.multiplayerTest.structures.WallStructure3;
import com.steve6472.multiplayerTest.structures.WallStructure4;
import com.steve6472.sge.gfx.Camera;
import com.steve6472.sge.gfx.Font;
import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.RenderMethods;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.Texture;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.callbacks.KeyCallback;
import com.steve6472.sge.main.game.EntityList;
import com.steve6472.sge.main.game.world.Chunk;
import com.steve6472.sge.main.game.world.GameCamera;
import com.steve6472.sge.main.game.world.World;
import com.steve6472.sge.main.networking.packet.DisconnectPacket;
import com.steve6472.sge.main.networking.packet.Packet;
import com.steve6472.sge.test.ShaderTest2;

public class Game extends MainApplication
{
	public ServerGui serverGui;
	public ClientGui clientGui;
	public RenderTestGui renderTestGui;
	
	public static int SERVER_CHUNK_SIZE = 8, SERVER_WORLD_SIZE = 64;
	
	public static InventoryRenderer inventoryRenderer;
	
	public static EntityList entityList;
	public static GameCamera camera;
	
	public static Model tileModel, fullModel;
	public static Shader shader, shaderSub;
	
	public static Shader fontShader;
	public static Model fontModel;
	
	public static Shader shader0;
	
	public static Shader tileShade;
	public static Shader rectShader;
	
	public static Model shadeModelLine0, shadeModelLine1, shadeModelLine2, shadeModelLine3;
	public static Model shadeModelCornerIn0, shadeModelCornerIn5, shadeModelCornerIn2, shadeModelCornerIn4;
	public static Model shadeModelCornerOut0, shadeModelCornerOut5, shadeModelCornerOut2, shadeModelCornerOut4;
	public static Model pixelModel, pixelModel64, pixelModel32, pixelModel16, pixelModel8, pixelModel4, pixelModel3, pixelModel2, pixelModel1;
	
	public static Sprite sprites;
	
	public static Structure[] structures;
	
	static
	{
		structures = new Structure[]
				{ 
						new WallStructure0(), 
						new WallStructure1(), 
						new Lake(), 
						new FlowerStructure(), 
						new WallStructure3(), 
						new WallStructure2(),
						new WallStructure4(),
						new Grass0(),
						new Grass1()
				};
	}
	
	public static final int spawnX = 32 * 4;
	public static final int spawnY = 32 * 4;
	
	public static Sprite tileSprite;
	
	public static HashMap<Integer, Shader> particleShaderList = new HashMap<Integer, Shader>();
	
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
		Packet.addPacket(8, 	CMouseButton.class);
		Packet.addPacket(9, 	CRotate.class);
		Packet.addPacket(10, 	CMovePacket.class);
		Packet.addPacket(11, 	SAddAnimation.class);
		Packet.addPacket(12, 	SChangeTile.class);
		Packet.addPacket(13, 	CSetRenderDistance.class);
		Packet.addPacket(14, 	SSetNetworkId.class);
		Packet.addPacket(15, 	SSetScore.class);
		Packet.addPacket(16, 	SSetName.class);
		Packet.addPacket(17, 	CSetName.class);
		Packet.addPacket(18, 	SChat.class);
		Packet.addPacket(19, 	CChat.class);
		Packet.addPacket(20, 	CUpdatePacket.class);
		Packet.addPacket(21, 	CPing.class);
		Packet.addPacket(22, 	SPingResponse.class);
		Packet.addPacket(23, 	SSetChunk.class);
		Packet.addPacket(24, 	CConfirmChunk.class);
		Packet.addPacket(25, 	SRunAnimation.class);
		Packet.addPacket(26, 	SAddEvent.class);
		Packet.addPacket(27, 	SRunEvent.class);
		Packet.addPacket(28, 	SRotate.class);
		Packet.addPacket(29,  	SInitClientData.class);
		Packet.addPacket(30,  	CChangeSlot.class);
		Packet.addPacket(31,  	SChangeSlot.class);
		Packet.addPacket(32,  	SOpenInventory.class);
		Packet.addPacket(33,  	CCloseInventory.class);
		Packet.addPacket(34,  	CMoveItem.class);
		Packet.addPacket(35,  	CKey.class);
		Packet.addPacket(36,  	CAllowPackets.class);
		
		//FIXME
//		GameTile.initGameTiles(ServerTile.getAtlas(), 32, 32, new Shader("shaders\\basev2"), 31, 17);
		
		sprites = new Sprite("sprites.png");
		tileSprite = new Sprite(new int[0], 0, 0);
		
		camera = new GameCamera();
		camera.setSize(getWidth(), getHeight());

		serverGui = new ServerGui(this);
		clientGui = new ClientGui(this);
		renderTestGui = new RenderTestGui(this);
		
		fontShader = new Shader("shaders\\basev2");
		fontModel = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(8, 8, getFont().getFont()), ShaderTest2.createArray(0));

		fullModel = new Model(ShaderTest2.fillScreen(), ShaderTest2.createBasicTexture(), ShaderTest2.createArray(0));

		shader = new Shader("shaders\\basev2");
		shaderSub = new Shader("shaders\\basev2Sub");
		pixelModel1 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(1, 1, sprites), ShaderTest2.createArray(0));
		pixelModel2 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(2, 2, sprites), ShaderTest2.createArray(0));
		pixelModel3 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(3, 3, sprites), ShaderTest2.createArray(0));
		pixelModel4 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(4, 4, sprites), ShaderTest2.createArray(0));
		pixelModel8 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(8, 8, sprites), ShaderTest2.createArray(0));
		pixelModel16 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(16, 16, sprites), ShaderTest2.createArray(0));
		pixelModel32 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(32, 32, sprites), ShaderTest2.createArray(0));
		pixelModel64 = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(64, 64, sprites), ShaderTest2.createArray(0));
		
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
		
		inventoryRenderer = new InventoryRenderer(this);
		
		for (ParticleType pt : ParticleType.values())
		{
			try
			{
				IParticle ip = pt.getClazz().newInstance();
				
				particleShaderList.put(pt.getParticleId(), ip.getShader());
				
			} catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		
		Helper.initHelper();
		RenderMethods.init(camera);

		getKeyHandler().addKeyCallback(new KeyCallback()
		{
			@Override
			public void invoke(int key, int scancode, int action, int mods)
			{
				if (clientGui != null && clientGui.client != null)
				{
					clientGui.client.sendPacket(new CKey(key, action, mods));
				}
				
				if (key == GLFW_KEY_F2 && action == GLFW_PRESS)
				{
					takeScreenshot();
				}
				
				if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
				{
					if (inventoryRenderer.isVisible())
					{
						inventoryRenderer.hideInventory();
						clientGui.client.sendPacket(new CCloseInventory());
					} else
					{
						exit();
					}
				}
			}
		});
		
//		addBasicResizeOrtho();
//		addWindowSizeCallback(new WindowSizeCallback()
//		{
//			@Override
//			public void invoke(int width, int height)
//			{
//				camera.setSize(width, height);
//			}
//		});
		addBasicResizeOrtho2();
		new MenuGui(this);
	}

	public static boolean isTileLocOutOfBounds(int x, int y)
	{
		return (x < 0 || y < 0 || x >= (World.worldWidth * Chunk.chunkWidth) || y >= (World.worldHeight * Chunk.chunkHeight));
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
				.translate(size * (mainApp.getCurrentWidth() / 32) - 1 - (float) x / size, size * (mainApp.getCurrentHeight() / 32) - 1 - (float) y / size, 0);
			
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
	
	public static void drawSprite(Model model, Shader shader, Sprite sprite)
	{
		Matrix4f target = new Matrix4f();
		
		Camera camera = Game.camera;
		
		camera.getProjection().mul(Helper.toMatrix(), target);

		if (sprite != null)
		{
			sprite.bind();
		}
		
		if (shader != null)
		{
			shader.bind();

			shader.setUniform1f("sampler", 0);
			shader.setUniform2f("texture", 0, 0);
			shader.setUniform4f("col", Helper.getRed(), Helper.getGreen(), Helper.getBlue(), Helper.getAlpha());

			shader.setUniformMat4f("projection", target);
		}
		
		model.render();
	}
	
	public static void drawSpriteFromAtlas2(float x, float y, Model model, Shader shader, Sprite sprite)
	{
		drawSpriteFromAtlas(1f / (float) sprite.getWidth() * x, 1f / (float) sprite.getHeight() * y, model, shader, sprite);
	}
	
	public static void drawSpriteFromAtlas(float indexX, float indexY, Model model, Shader shader, Sprite sprite)
	{
		if (!Helper.isInitialised())
			return;
		
		Matrix4f target = new Matrix4f();
		
		Camera camera = Game.camera;
		
		camera.getProjection().mul(Helper.toMatrix(), target);
		
		if (sprite != null)
			sprite.bind();
		
		if (shader != null)
		{
			shader.bind();
			
			shader.setUniform1f("sampler", 0);
			shader.setUniform2f("texture", indexX, indexY);
			shader.setUniform4f("col", Helper.getRed(), Helper.getGreen(), Helper.getBlue(), Helper.getAlpha());
			
			shader.setUniformMat4f("projection", target);
		}
		
		model.render();
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
		
		fullModel.render();
	}

	@Override
	public void tick()
	{
		camera.setSize(getCurrentWidth(), getCurrentHeight());
		inventoryRenderer.tick(this);
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
	protected boolean escExit()
	{
		return false;
	}

	@Override
	public void setWindowHints()
	{
//		glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
//		glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
	}
	
	public static void main(String[] args)
	{
		new Game();
	}
	
	@Override
	protected boolean disableGlDepthTest()
	{
		return true;
	}
	
}

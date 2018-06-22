/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.gui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.SmallNineSlice;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.Tessellator;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.MainApplication;
import com.steve6472.sge.main.SGArray;
import com.steve6472.sge.main.Util;
import com.steve6472.sge.main.callbacks.KeyCallback;
import com.steve6472.sge.main.game.Vec2;

public class RenderTestGui extends Gui implements KeyList
{

	private static final long serialVersionUID = -7181572382358516523L;
	
	SmallNineSlice ans;

	public RenderTestGui(MainApplication mainApp)
	{
		super(mainApp);
	}
	
//	Button b;
	
	SmallNineSlice hotbar;
	
	SGArray<Vec2> points;
	SGArray<float[]> colors;
	
	byte selected = 0;
	float[] rgb;
	int mode = 0;
	
	boolean randomColors = true;
	
	Model model;
	
	@Override
	public void showEvent()
	{
//		ans = new SmallNineSlice(6, 0, 6, 6, MainApplication.sprites, Game.shader, Game.camera);
//		ans.setCorner(0, 0, 2, 2);
//		ans.setTopEdge(2, 0, 2, 2, 2);
//		ans.setSideEdge(0, 2, 2, 2, 2);
//		ans.createMiddle();
//		ans.setScaleMultiplier(2f, 2f);
//		ans.setScale(getMainApp().getWidth() / 4f - 3f, getMainApp().getHeight() / 4f - 3f, 1);
//		
//		hotbar = new SmallNineSlice(97, 97, 30, 30, Game.sprites, Game.shader, Game.camera);
//		hotbar.setCorner(0, 0, 7, 7);
//		hotbar.setTopEdge(7, 0, 16, 7, 0f);
//		hotbar.setSideEdge(0, 7, 7, 16, 0f);
//		hotbar.createMiddle();
		
//		img = new Sprite("tiles\\rainbow.png");
		img = new Sprite("*atlas.png");
		sha = new Shader("shaders\\tess");
		pix = new Sprite(new int[] {0xff000000}, 1, 1);
		
		points = new SGArray<Vec2>(0);
		colors = new SGArray<float[]>(0);
		rgb = new float[] {1, 1, 1};
		
		ver = new ArrayList<Float>();
		tex = new ArrayList<Float>();
		col = new ArrayList<Float>();
		
		gen(3, 3);
		
		float[] c = new float[col.size()];
		for (int i = 0; i < col.size(); i++)
			c[i] = col.get(i);
		
		float[] t = new float[tex.size()];
		for (int i = 0; i < tex.size(); i++)
			t[i] = tex.get(i);
		
		float[] v = new float[ver.size()];
		for (int i = 0; i < ver.size(); i++)
			v[i] = ver.get(i);
		
		model = new Model(v, t, c);
		model.setMode(Tessellator.TRIANGLE_STRIP);
		
//		b = new Button(0, 0, 30, 10, 2, getMainApp(), Game.shader, Game.camera);
//		glMatrixMode(GL_MODELVIEW);
		
		getMainApp().resetOrtho();
		
		getMainApp().getKeyHandler().addKeyCallback(new KeyCallback()
		{
			@Override
			public void invoke(int key, int scancode, int action, int mods)
			{
				if (action == GLFW.GLFW_PRESS)
				{
					if (key == A)
					{
						points.addObject(new Vec2(getMouseHandler().getMouseX(), getMouseHandler().getMouseY()));
						colors.addObject(rgb.clone());
					}
					
					if (key == C)
					{
						points.clear();
						colors.clear();
					}
					
					if (key == P)
					{
						gen(3, 3);
						
						float[] c = new float[col.size()];
						for (int i = 0; i < col.size(); i++)
							c[i] = col.get(i);
						
						float[] t = new float[tex.size()];
						for (int i = 0; i < tex.size(); i++)
							t[i] = tex.get(i);
						
						float[] v = new float[ver.size()];
						for (int i = 0; i < ver.size(); i++)
							v[i] = ver.get(i);
						
//						model.changeData(v, t, c);
					}
					
					if (key == L)
					{
						float[] c = new float[col.size()];
						for (int i = 0; i < col.size(); i++)
							c[i] = col.get(i);
						
						float[] t = new float[tex.size()];
						for (int i = 0; i < tex.size(); i++)
							t[i] = tex.get(i);
						
						float[] v = new float[ver.size()];
						for (int i = 0; i < ver.size(); i++)
							v[i] = ver.get(i);
						
						int i = 4;
						
						t[i * 8 + 0] = 0f;
						t[i * 8 + 1] = 0f;
						t[i * 8 + 2] = 0f;
						t[i * 8 + 3] = 0f;
						t[i * 8 + 4] = 0f;
						t[i * 8 + 5] = 0f;
						t[i * 8 + 6] = 0f;
						t[i * 8 + 7] = 0f;

//						model.changeData(v, t, c);
					}
					
					if (key == K)
					{
						randomColors = !randomColors;
					}
					
					if (key == R)
						selected = 0;
					if (key == G)
						selected = 1;
					if (key == B)
						selected = 2;
					
					if (key == GLFW.GLFW_KEY_KP_ADD)
					{
						if (mods == GLFW.GLFW_MOD_SHIFT)
						{
							if (++mode > 9)
								mode = 9;
							System.out.println(Tessellator.getRenderModeName(mode));
						} else
						{
							rgb[selected] += 0.25;

							if (rgb[selected] > 1)
								rgb[selected] = 1;
						}
					}

					if (key == GLFW.GLFW_KEY_KP_SUBTRACT)
					{
						if (mods == GLFW.GLFW_MOD_SHIFT)
						{
							if (--mode < 0)
								mode = 0;
							System.out.println(Tessellator.getRenderModeName(mode));
						} else
						{
							rgb[selected] -= 0.25;

							if (rgb[selected] < 0)
								rgb[selected] = 0;
						}
					}
				}
			}
		});
	}
	
	public void gen(int sizeX, int sizeZ)
	{
		ver.clear();
		tex.clear();
		col.clear();

		sizeX = 3;
		sizeZ = 3;
		
		for (float i = 0; i < sizeX; i++)
		{
			for (float j = 0; j < sizeZ; j++)
			{
				float inX = 5;
				float inY = 4;
			
				inX++;
				inY++;
				
				float sx = 1f / 6f;
				float sy = 1f / 6f;
				
				float x = sx * inX;
				float y = sy * inY;
				
				float X = sx * (inX - 1);
				float Y = sy * (inY - 1);
				
				add(0 + i, 0 + j, X, y);
				add(1 + i, 0 + j, x, y);
				add(1 + i, 1 + j, x, Y);
				add(0 + i, 1 + j, X, Y);
			}
		}
	}
	
	List<Float> ver;
	List<Float> tex;
	List<Float> col;
	
	private void add(float x, float y, float tx, float ty)
	{
		ver.add(x);
		ver.add(y);
		
		tex.add(tx);
		tex.add(ty);

		col.add(0f);
		col.add(0f);
		col.add(0f);
		col.add(0f);
	}

	Sprite img;
	Shader sha;
	Sprite pix;

	@Override
	public void createGui()
	{
	}
	
	int delay = 0;

	@Override
	public void guiTick()
	{
		if (randomColors)
		{
			delay++;
			if (delay == 10)
			{
				delay = 0;
				rgb[0] = Util.getRandomFloat(1, 0);
				rgb[1] = Util.getRandomFloat(1, 0);
				rgb[2] = Util.getRandomFloat(1, 0);
			}
		}
	}
	
	@Override
	public void render(Screen screen)
	{
		float w = getMainApp().getCurrentWidth();
		float h = getMainApp().getCurrentHeight();
		
		float size = 32 * 3;
		
		sha.setUniformMat4f("projection", 
				new Matrix4f()
				.translate(-0.75f, -0.75f, 0)
				.scale(1f / w * size, 1f / h * size, 0));
		
		img.bind();
		sha.bind();
		model.setMode(Tessellator.QUADS);
		model.render();
		
//		Matrix4f projectionMatrix;
		
//		Helper.pushLayer();
		
//		sha.bind();
		
//		Matrix4f target = new Matrix4f();
		
//		Game.camera.getProjection().mul(Helper.toMatrix(), target);

//		sha.setUniform4f("col", 1, 1, 1, 1);
//		sha.setUniformMat4f("projection", target);
		
//		RenderMethods.prepare();
//		RenderMethods.setPixelPerfect(true);
//		RenderMethods.drawSprite(5, 5, img);
		
//		RenderMethods.drawSquare(0, 0, 16, 16, ColorUtil.getColor(rgb[0], rgb[1], rgb[2], 1));
		/*
		Tessellator tess = Tessellator.getInstance();
//
		
		float mx = getMainApp().getMouseX();
		float my = getMainApp().getMouseY();
		
		for (Vec2 v : points)
		{
			float[] rgb = colors.getObject(points.getIterIndex());
			tess.putPixelPerfect(v.getIntX(), v.getIntY(), 0, 0, rgb[0], rgb[1], rgb[2], 1, Game.camera);
		}
		tess.putPixelPerfect(mx, my, 0, 0, rgb[0], rgb[1], rgb[2], 1, Game.camera);
		
		pix.bind(0);
		sha.bind();
		
		sha.setUniform1f("sampler", 0);
		sha.setUniform2f("texture", 0, 0);
		sha.setUniform4f("col", 0, 0, 0, 0);
		sha.setUniformMat4f("projection", new Matrix4f());
//		GL11.glLineWidth(8f);
		tess.render(Tessellator.getRenderMode(mode));*/
//		tess.render(Tessellator.TRIANGLE_FAN);
		
		
//		/*tess.put(-1, 1, 0, 0, 1, 0, 0, 1);*/	coord(tess, 0, 0, 1, 0, 0, 1);
//		/*tess.put(1, 1, 0, 1, 0, 1, 0, 1);*/	coord(tess, s, s, 0, 1, 0, 1);
//		/*tess.put(1, -1, 1, 1, 0, 0, 1, 1);*/	coord(tess, 0, s, 0, 0, 1, 1);
		/*tess.put(-1, -1, 1, 1, 1, 1, 1, 1f);*///coord(tess, -1, -1, 1, 1, 1, 1);
		
//		tri(tess, new float[] {0, 0,
//							   s, s,
//							   0, s},
//				
//				new float[] {1, 0, 0, 1,
//							 0, 1, 0, 1,
//							 0, 0, 1, 1});

//		img.bind(0);
//		sha.bind();
//		
//		sha.setUniform1f("sampler", 0);
//		sha.setUniform2f("texture", 0, 0);
//		sha.setUniform4f("col", 0, 0, 0, 0);
//		sha.setUniformMat4f("projection", new Matrix4f());
//
//		tess.put(-1, 1, 0, 0, 1, 0, 0, 1);
//		tess.put(1, -1, 1, 0, 0, 1, 0, 1);
//		tess.put(-1, -1, 1, 1, 0, 0, 1, 1);
//		
//		tess.render(Tessellator.TRIANGLES);

//		float m = 1f;
//		quad(tess, (Game.camera.getWidth() - Game.camera.getWidth() * m) / 2f, (Game.camera.getHeight() - Game.camera.getHeight() * m) / 2f,
//				Game.camera.getWidth() * m, Game.camera.getHeight() * m);
//		tess.render(tess.TRIANGLES);
//		
//		quad(tess, 0, 0, 256, 256);
//		tess.put(-1, 1, 	0, 0, 0, 0, 1, 0);
//		tess.put(1, -1, 	1, 1, 0, 0, 0, 0);
//		tess.put(-1, -1, 	0, 0, 0, 1, 0, 0);
//		tess.put(1, 1, 		1, 1, 0, 0, 0, 0);
//
		/*
		float mx = getMainApp().getMouseX();
		float my = getMainApp().getMouseY();
//		
		tess.putPixelPerfect(mx - 32, my - 32, 				0, 0, 1, 0, 0, 1, Game.camera);
		tess.putPixelPerfect(64 + mx - 32, my - 32, 		1, 0, 0, 1, 0, 1, Game.camera);
		tess.putPixelPerfect(64 + mx - 32, 64 + my - 32, 	1, 1, 1, 0, 1, 1, Game.camera);
		tess.putPixelPerfect(mx - 32, 64 + my - 32, 		0, 1, 0, 1, 0, 1, Game.camera);
//		
		img.bind(0);
		sha.bind();
		
		sha.setUniform1f("sampler", 0);
		sha.setUniform2f("texture", 0, 0);
		sha.setUniform4f("col", 0, 0, 0, 0);
		sha.setUniformMat4f("projection", new Matrix4f());
//		GL11.glLineWidth(8f);
//		tess.render(GL11.GL_LINE_STRIP);
		tess.render(GL11.GL_TRIANGLE_FAN);*/
		
//		Helper.popLayer();
		
	}
	
	/*
	public void quad(Tessellator tess, float x, float y, float w, float h)
	{
		float W = Game.camera.getWidth();
		float H = Game.camera.getHeight();
		
		w *= 2f;
		h *= 2f;
		
		x *= 2f;
		y *= -2f;
		x -= W / 2f - w;
		
		float fx = W / 2f + w / 2f;
		float fy = -H + h / 2f;
		
		coord(tess, x - w, y, 		1, 0, 0, 1, -w / 2f, -h / 2f, fx, fy, 0, 0);
		coord(tess, x, y, 			0, 1, 0, 1, -w / 2f, -h / 2f, fx, fy, 1, 0);
		coord(tess, x, y - h, 		0, 0, 1, 1, -w / 2f, -h / 2f, fx, fy, 1, 1);
		coord(tess, x - w, y - h, 	1, 1, 1, 1, -w / 2f, -h / 2f, fx, fy, 0, 1);
	}
	
	public void coord(Tessellator tess, float x, float y, float r, float g, float b, float a, float rx, float ry, float fx, float fy, int tx, int ty)
	{
		float w = 1f / (float) Game.camera.getWidth();
		float h = 1f / (float) Game.camera.getHeight();
		
		tess.put(w * x - w * rx - w * fx, h * y - h * ry - h * fy, tx, ty, r, g, b, a);
	}*/

}

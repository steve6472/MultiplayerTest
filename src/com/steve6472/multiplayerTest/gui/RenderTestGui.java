/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.gui;


import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.steve6472.multiplayerTest.Button;
import com.steve6472.multiplayerTest.Game;
import com.steve6472.sge.gfx.Screen;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.SmallNineSlice;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.gfx.Tessellator;
import com.steve6472.sge.gui.Gui;
import com.steve6472.sge.main.MainApplication;

public class RenderTestGui extends Gui
{

	private static final long serialVersionUID = -7181572382358516523L;
	
	SmallNineSlice ans;

	public RenderTestGui(MainApplication mainApp)
	{
		super(mainApp);
	}
	
	Button b;
	
	SmallNineSlice hotbar;
	
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
		
		img = new Sprite("tiles\\rainbow.png");
		sha = new Shader("shaders\\tess");
		pix = new Sprite(new int[] {0xff000000}, 1, 1);
		
//		b = new Button(0, 0, 30, 10, 2, getMainApp(), Game.shader, Game.camera);
//		glMatrixMode(GL_MODELVIEW);
		
		getMainApp().resetOrtho();
	}
	
	Sprite img;
	Shader sha;
	Sprite pix;

	@Override
	public void createGui()
	{
	}
	

	@Override
	public void guiTick()
	{
	}
	
	@Override
	public void render(Screen screen)
	{
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
		
		Tessellator tess = Tessellator.getInstance();
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
		tess.render(GL11.GL_TRIANGLE_FAN);
		
//		Helper.popLayer();
		
	}
	
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
	}

}

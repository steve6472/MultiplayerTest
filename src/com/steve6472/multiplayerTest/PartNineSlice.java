/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 21. 4. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest;

import org.joml.Matrix4f;

import com.steve6472.sge.gfx.Helper;
import com.steve6472.sge.gfx.Model;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.test.Camera;
import com.steve6472.sge.test.ShaderTest2;

public class PartNineSlice
{
	Sprite atlas;
	
	float offsetX;
	float offsetY;
	float totalWidth;
	float totalHeight;
	
	float scaleMultiplierX = 1;
	float scaleMultiplierY = 1;
	
	float scaleX;
	float scaleY;
	
	public PartNineSlice(float x, float y, float width, float height, Sprite atlas)
	{
		this.offsetX = x / (float) atlas.getWidth();
		this.offsetY = y / (float) atlas.getHeight();
		this.totalWidth = width;
		this.totalHeight = height;
		this.atlas = atlas;
	}
	
	public PartNineSlice setScaleMultiplier(float scaleMultiplierX, float scaleMultiplierY)
	{
		this.scaleMultiplierX = scaleMultiplierX;
		this.scaleMultiplierY = scaleMultiplierY;
		return this;
	}
	
	public PartNineSlice setScale(int x, int y)
	{
		this.scaleX = x;
		this.scaleY = y;
		
		return this;
	}
	
	float cornerX, cornerY, cornerWidth, cornerHeight;
	Model cornerModel;
	
	public PartNineSlice setCorner(float x, float y, float width, float height)
	{
		this.cornerX = x / (float) atlas.getWidth();
		this.cornerY = y / (float) atlas.getHeight();
		this.cornerWidth = width;
		this.cornerHeight = height;
		cornerModel = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(width, height, atlas), ShaderTest2.createArray(0));
		return this;
	}
	
	float topEdgeX, topEdgeY, topEdgeWidth, topEdgeHeight;
	Model topEdgeModel;
	
	public PartNineSlice setTopEdge(float x, float y, float width, float height, float tem)
	{
		this.topEdgeX = x / (float) atlas.getWidth();
		this.topEdgeY = y / (float) atlas.getHeight();
		this.topEdgeWidth = width;
		this.topEdgeHeight = height;
		this.tem = tem;
		topEdgeModel = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(width, height, atlas), ShaderTest2.createArray(0));
		return this;
	}
	
	float sideEdgeX, sideEdgeY, sideEdgeWidth, sideEdgeHeight;
	Model sideEdgeModel;
	
	public PartNineSlice setSideEdge(float x, float y, float width, float height, float sem)
	{
		this.sideEdgeX = x / (float) atlas.getWidth();
		this.sideEdgeY = y / (float) atlas.getHeight();
		this.sideEdgeWidth = width;
		this.sideEdgeHeight = height;
		this.sem = sem;
		sideEdgeModel = new Model(ShaderTest2.fillScreen(), ShaderTest2.createTexture(width, height, atlas), ShaderTest2.createArray(0));
		return this;
	}
	
	Model middleModel;
	
	public PartNineSlice createMiddle()
	{
		middleModel = new Model(ShaderTest2.fillScreen(),
				ShaderTest2.createTexture(topEdgeWidth, sideEdgeHeight, atlas), ShaderTest2.createArray(0));
		return this;
	}
	
	public void render(float x, float y)
	{
		float w = (float) atlas.getWidth();
		float h = (float) atlas.getHeight();
		
		float sx = scaleX / cornerWidth * 2;
		float sy = scaleY / cornerHeight * 2;
		
		renderCorners(sx, sy, w, h, x, y);
		for (int i = 0; i <= scaleY; i++)
		{
			float f = i * 2f - scaleY;
			renderSideEdges(sx, sy, w, h, x, y - sideEdgeHeight * scaleMultiplierY / 2f * f);
		}
		for (int i = 0; i <= scaleX; i++)
		{
			float f = i * 2f - scaleX;
			renderTopEdges(sx, sy, w, h, x - topEdgeWidth * scaleMultiplierX / 2f * f, y);
		}
		
		Helper.pushLayer();
		Helper.translate(x, y, 0);
		Helper.scale(scaleX + 1f, scaleY + 1f, 0);
		Helper.scale(topEdgeWidth * scaleMultiplierX / 2f, sideEdgeHeight * scaleMultiplierY / 2f, 0);
		
		draw(offsetX + topEdgeX, offsetY + sideEdgeY, 0, 0, middleModel, Game.shader);

		sem = topEdgeWidth / cornerWidth + 1f;
		tem = sideEdgeHeight / cornerHeight + 1f;
		
		Helper.popLayer();
	}
	
	float sem = 1f;
	float tem = 1f;
	
	private void renderTopEdges(float sx, float sy, float w, float h, float x, float y)
	{
		Helper.pushLayer();
		Helper.translate(x, y, 0);
		Helper.scale(scaleX + 1f, 1, 0);
		Helper.scale(topEdgeWidth * scaleMultiplierX / 2f, topEdgeHeight * scaleMultiplierY / 2f, 0);
		
		Helper.translate(0, tem, 0);
		draw(offsetX + topEdgeX, offsetY, 0, sideEdgeHeight * scaleY / 4f, topEdgeModel, Game.shader);
		
		Helper.translate(0, tem * -2, 0);
		draw(offsetX + topEdgeX, offsetY + sideEdgeY + sideEdgeHeight / h, 0, -sideEdgeHeight * scaleY / 4f, topEdgeModel, Game.shader);
		
		Helper.popLayer();
	}
	
	private void renderSideEdges(float sx, float sy, float w, float h, float x, float y)
	{
		Helper.pushLayer();
		Helper.translate(x, y, 0);
		Helper.scale(1, scaleY + 1f, 0);
		Helper.scale(sideEdgeWidth * scaleMultiplierX / 2f, sideEdgeHeight * scaleMultiplierY / 2f, 0);

		Helper.translate(sem, 0, 0);
		draw(offsetX, offsetY + sideEdgeY, scaleX / 4f * topEdgeWidth, 0, sideEdgeModel, Game.shader);
		
		Helper.translate(sem * -2, 0, 0);
		draw(offsetX + topEdgeX + topEdgeWidth / w, offsetY + sideEdgeY, -scaleX / 4f * topEdgeWidth, 0, sideEdgeModel, Game.shader);
		
		Helper.popLayer();
	}
	
	private void renderCorners(float sx, float sy, float w, float h, float x, float y)
	{
		Helper.pushLayer();
		Helper.translate(x, y, 0);
		Helper.scale(cornerWidth * scaleMultiplierX / 2f, cornerHeight * scaleMultiplierY / 2f, 0);
		
		Helper.translate(sem, tem, 0);
		draw(offsetX, offsetY, scaleX / 4f * topEdgeWidth, scaleY / 4f * sideEdgeHeight, cornerModel, Game.shader);
		
		Helper.translate(sem * -2f, 0, 0);
		draw(offsetX + topEdgeX + topEdgeWidth / w, offsetY, -scaleX / 4f * topEdgeWidth, scaleY / 4f * sideEdgeHeight, cornerModel, Game.shader);
		
		Helper.translate(sem * 2f, tem * -2f, 0);
		draw(offsetX, offsetY + sideEdgeY + sideEdgeHeight / h, scaleX / 4f * topEdgeWidth, -scaleY / 4f * sideEdgeHeight, cornerModel, Game.shader);
		
		Helper.translate(sem * -2f, 0, 0);
		draw(offsetX + topEdgeX + topEdgeWidth / w, offsetY + sideEdgeY + sideEdgeHeight / h, -scaleX / 4f * topEdgeWidth, -scaleY / 4f * sideEdgeHeight, cornerModel, Game.shader);
		
		Helper.popLayer();
	}
	
	private void draw(float indexX, float indexY, float tx, float ty, Model model, Shader shader)
	{
		if (!Helper.isInitialised())
			return;
		
		Matrix4f target = new Matrix4f();
		
		Camera camera = Game.camera;
		
		if (tx != 0 || ty != 0)
		{
			Helper.translate(tx, ty, 0);
		}

		camera.getProjection().mul(Helper.toMatrix(), target);
		
		if (tx != 0 || ty != 0)
		{
			Helper.translate(-tx, -ty, 0);
		}
		
		atlas.bind();
		
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
}

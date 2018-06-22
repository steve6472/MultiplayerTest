/**********************
* Created by steve6472 (Mirek Jozefek)
* On date: 22. 6. 2018
* Project: MultiplayerTest
*
***********************/

package com.steve6472.multiplayerTest.particles;

import org.joml.Matrix4f;

import com.steve6472.multiplayerTest.Game;
import com.steve6472.multiplayerTest.IParticle;
import com.steve6472.multiplayerTest.gui.ClientGui;
import com.steve6472.sge.gfx.Shader;
import com.steve6472.sge.gfx.Sprite;
import com.steve6472.sge.main.game.particle.AngledParticle;
import com.steve6472.sge.main.game.world.GameCamera;
import com.steve6472.sge.main.game.world.GameTile;

public class TileCrackParticle extends AngledParticle implements IParticle
{
	private static final long serialVersionUID = -3140896267094406070L;
	
	private float startLife;
	private int tileIndexX, tileIndexY;
	private float rotation;
	
	public TileCrackParticle()
	{
		super(0, 0, 0, 0, null);
	}

	public TileCrackParticle(double x, double y, double angle, int life, int tileIndexX, int tileIndexY, double speed, float rotation)
	{
		super(x, y, angle, life, null);
		startLife = life;
		this.tileIndexX = tileIndexX;
		this.tileIndexY = tileIndexY;
		this.rotation = rotation;
		setSpeed(speed);
	}

	@Override
	public void render()
	{
		GameCamera camera = Game.camera;
		
		float w = (float) camera.getWidth();
		float h = (float) camera.getHeight();
		
		float W = 1f / w * 4 * 2f;
		float H = 1f / h * 4 * 2f;
		
		Matrix4f mat = new Matrix4f()
//				.translate(-getIntX() + camera.getX(), -getIntY() + camera.getY(), 0)
				//Render particle at top left corner
				.translate(-1, 1, 0)
				//Move particle by players position in the world & it's position
				.translate((1f / w) * (getIntX() - camera.getX()) * 2f, (1f / h) * (-getIntY() + camera.getY()) * 2f, 0)
				//Scale it so we can actually see it
				.scale(W, H, 0)
				//Rotate
				.rotate(rotation, 0, 0, 1);

		//& Finally draw
		
		//Set texture
		shader.setUniform2f("texture", (float) tileIndexX / ClientGui.atlas.getSize(), (float) tileIndexY / ClientGui.atlas.getSize());
		
		//Change transparency
		shader.setUniform4f("col", 0.0f, 0.0f, 0.0f, (1f / (float) startLife) * (float) life - 0.95f);
			
		//Apply transformation matrix
		shader.setUniformMat4f("transformation", mat);

		Game.tileModel.render();
	}

	@Override
	public Sprite getSprite()
	{
		return ClientGui.atlas.getAtlas();
	}
	
	private static final Shader shader = new Shader(""
			+ "#version 330 core														\n"

			+ "layout(location = 0) in vec2 position;									\n"
			+ "layout(location = 1) in vec2 texture;									\n"
			+ "layout(location = 2) in vec4 color;										\n"
			
			+ "uniform mat4 transformation;												\n"
			
			+ "out vec4 vColor;															\n"
			+ "out vec2 vTexture;														\n"
			
			+ "void main()																\n"
			+ "{																		\n"
			+ "		vTexture = texture;													\n"
			+ "		gl_Position = transformation * vec4(position, 0.0, 1.0);			\n"
			+ "}"
			
			, ""
			
			+ "#version 330 core														\n"
					
			+ "uniform sampler2D sampler;												\n"
			+ "uniform vec2 texture;													\n"
			+ "uniform vec4 col;														\n"
			
			+ "in vec2 vTexture;														\n"
			
			+ "out vec4 fragColor;														\n"
			
			+ "void main()"
			+ "{"
			+ "		fragColor = texture2D(sampler, vTexture + texture) + col;			\n"
			+ "}"
			);

	@Override
	public Shader getShader()
	{
		return shader;
	}

}

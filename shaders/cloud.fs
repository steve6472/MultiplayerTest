#version 330 core

#include shaders\\noise2D.glsl

uniform sampler2D tex0;

uniform float x;
uniform float y;

in vec2 vTexture;
in vec4 glPos;

out vec4 fragColor;

void main()
{
	vec4 texColor0 = texture(tex0, vTexture);
	
	float mask = snoise(vec2(glPos.x + x, glPos.y + y));
	
	//float mask = texture(mask, vTexture).a;

	fragColor = vec4(mask, mask, mask, 0.5);
	//fragColor = texture(tex0, vTexture) + mask;
}
#version 330 core

uniform sampler2D sampler;

in vec4 vColor;
in vec2 vTexture;

out vec4 fragColor;

void main()
{
	fragColor = texture2D(sampler, vTexture) + vColor;
}
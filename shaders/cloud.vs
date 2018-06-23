#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec4 color;

out vec2 vTexture;
out vec4 glPos;

void main()
{
    vTexture = texture;
    gl_Position = vec4(position, 0.0, 1.0);
	glPos = gl_Position;
}
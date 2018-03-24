#version 330 core

layout(location = 0) in vec2 position;
layout(location = 2) in vec4 color;

uniform mat4 projection;

out vec4 vColor;

void main()
{
	vColor = color;
    gl_Position = projection *  vec4(position, 0.0, 1.0);
}
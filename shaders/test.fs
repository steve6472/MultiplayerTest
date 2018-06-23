#version 330 core

uniform sampler2D sampler;
uniform float a;
uniform float b;
uniform float c;
uniform float d;

in vec2 vTexture;

out vec4 fragColor;

vec2 SineWave(vec2 p)
{
/*

		shader.setUniform1f("a", (float) ((Math.sin((double) time) + 2f) / 60f)); //octaves
		shader.setUniform1f("b", 4f); //Count of waves
		shader.setUniform1f("c", time); //Offset
		shader.setUniform1f("d", 1f); //Time offset
		
*/
    float pi = 3.14159;
    float w = b * pi;
    float t = c * pi / d;
    float y = sin(w * p.x + t) * a;
    return vec2(p.x, p.y + y);
}

void main()
{
	vec2 p = vTexture;
	vec2 uv = SineWave(p);
	fragColor = texture2D(sampler, uv);
}
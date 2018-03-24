#version 330 core

uniform sampler2D sampler;
uniform vec2 res;
uniform float radius;

float softness = 0.45;

in vec4 vColor;
in vec2 vTexture;

out vec4 fragColor;

void main()
{
	vec2 position = (gl_FragCoord.xy / res.xy) - vec2(0.5);
	
	vec4 texColor = texture2D(sampler, vTexture);
	
	//makes circle be always circle
	//position.x *= res.x / res.y;
	
	float len = length(position);
	
	float vignette = smoothstep(radius, softness, len);
	
	//fragColor = vec4(vec3(step(radius, len)), 1.0);
	
	texColor.rgb *= vignette;
	
	fragColor = texColor;
	
	//fragColor = texture2D(sampler, vTexture) + vColor;
}
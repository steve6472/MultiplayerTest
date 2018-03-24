#version 330 core

uniform float radius;
uniform vec2 res;
uniform vec4 lightColor;
uniform vec2 lightLocation;

uniform float softness;

in vec4 vColor;
in vec2 vTexture;
in vec2 vPosition;

out vec4 fragColor;

void main()
{
	vec2 position = (gl_FragCoord.xy / res.xy) - vec2(0.5);
	
	//position.x *= res.x / res.y;
	
	float len = length(position);
	
	float vig = smoothstep(radius, softness, len);
	
	//texColor.rgb *= vig;

	//float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
	//float gray = dot(texColor.rgb, vec3(1, 1, 1));
	fragColor = vec4(vec3(vig) * lightColor, lightColor.w);
	
	//fragColor = texColor;
	//fragColor = vec4(vec3(gray) * color, 1);

	//fragColor = texture2D(sampler, vTexture) + vColor;
}
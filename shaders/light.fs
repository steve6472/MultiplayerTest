#version 330 core

uniform float radius;
uniform vec2 res;
uniform vec3 lightColor;
uniform vec2 lightLocation;


vec3 ambientLight = vec3(0.1);
vec3 baseColor = vec3(0.2, 0.2, 0.2);


uniform float softness;

in vec4 vColor;
in vec2 vTexture;
//in vec2 vPosition;

out vec4 fragColor;

void main()
{
	vec4 light = vec4(ambientLight, 1);
	vec4 color = vec4(baseColor, 1);

	float distance = length(lightLocation - gl_FragCoord.xy);
	float attenuation = radius / distance;
	vec4 lColor = vec4(attenuation, attenuation, attenuation, attenuation) * vec4(lightColor, 1);
	color += lColor;

	fragColor = color * light;
}

/*
void main()
{
	vec2 position = (gl_FragCoord.xy / res.xy) - vec2(0.5);
	
	position.x *= res.x / res.y;
	
	vec4 texColor = texture2D(sampler, vTexture);
	
	float len = length(position);
	
	float vig = smoothstep(radius, softness, len);
	
	texColor.rgb *= vig;

	//float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
	float gray = dot(texColor.rgb, vec3(1, 1, 1));
	
	//fragColor = texColor;
	fragColor = vec4(vec3(gray) * color, 1);

	//fragColor = texture2D(sampler, vTexture) + vColor;
}*/
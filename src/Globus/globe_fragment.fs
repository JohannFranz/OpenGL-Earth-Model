#version 330

in vec3 outColor;
in vec2 outTexCoord;
in vec3 outNormal;
in vec3 outTangent;
in vec3 outBinormal;
in vec3 outPosition;
out vec4 fragColor;
				
vec4 calcShading( vec3 lightPos, vec3 position, vec3 normal, vec4 color ) 
{
	vec4 specularColor = vec4(1.0, 1.0, 1.0, 1.0); 
	float specular = 0;
	float specularIntensity = 0.5; 
	float diffuseIntensity = 0.5;
						
	//calc diffuse color
	vec3 lightDir = normalize(lightPos - position);
	float lambertian = diffuseIntensity * max(dot(lightDir,normal), 0); 
	color = color*lambertian;
						
	//calc specular color
	if(lambertian > 0) 
	{
		vec3 reflectDir = normalize(2*dot(normal, lightDir)*normal-lightDir); 
		vec3 viewDir = normalize(vec3(0,0,3)-position); 

		float specAngle = max(dot(reflectDir, viewDir), 0);
		specular = specularIntensity * pow(specAngle, 40);
	}

	return color + specular*specularColor;	
}
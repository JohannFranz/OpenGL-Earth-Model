#version 330

layout (location =0) in vec3 position;
layout (location =1) in vec3 color;
layout (location =2) in vec2 texCoord;
layout (location =3) in vec3 normal;
layout (location =4) in vec3 tangent;

out vec2 outTexCoord;
out vec3 outColor;
out vec3 outNormal;
out vec3 outTangent;
out vec3 outBinormal;
out vec3 outPosition; 

uniform mat4 projectionMatrix;
uniform mat4 rotationMatrix;

void main() 
{
	vec4 temp = rotationMatrix * vec4(position, 1.0);
	outPosition = temp.xyz;
	temp = temp	+ vec4(0,0,-3, 0);
	gl_Position = projectionMatrix * temp;
	outPosition = position;
	outTexCoord = texCoord;
	outNormal = normal;
	outTangent = tangent;
	outBinormal = cross(normal, tangent);
	outColor = color;
}
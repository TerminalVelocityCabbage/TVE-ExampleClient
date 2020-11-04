#version 330 core
layout (location = 0) in vec3 inPosition;
//TODO remove color info from vertex and handle with material
layout (location = 1) in vec4 inColor;
layout (location = 2) in vec2 inTextureCoord;
layout (location = 3) in vec3 vertexNormal;

out vec4 vertColor;
out vec2 vertTextureCoord;
out vec3 vertVertexNormal;
out vec3 vertVertexPosition;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform mat4 normalTransformationMatrix;

void main() {
   vec4 modelViewPosition = modelViewMatrix * vec4(inPosition, 1.0);
   gl_Position = projectionMatrix * modelViewPosition;
   vertColor = inColor;
   vertTextureCoord = inTextureCoord;
   vertVertexNormal = (normalTransformationMatrix * vec4(vertexNormal, 0.0)).xyz;
   vertVertexPosition = inPosition.xyz;
}

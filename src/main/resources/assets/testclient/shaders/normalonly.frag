#version 330 core

in vec4 vertColor;
in vec2 vertTextureCoord;
in vec3 vertVertexNormal;
in vec3 vertVertexPosition;

out vec4 fragColor;

uniform sampler2D textureSampler;

vec4 ambientColor;
vec4 diffuseColor;
vec4 specularColor;

void main() {
    fragColor = vec4(vertVertexNormal, 1.0);
}

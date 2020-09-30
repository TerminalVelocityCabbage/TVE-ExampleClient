#version 330 core

in vec4 vertColor;
in vec2 vertTextureCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
   fragColor = texture(textureSampler, vertTextureCoord) * vertColor;
}

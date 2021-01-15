#version 330 core
in vec2 vertTextureCoord;
in vec3 vertVertexColor;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
   fragColor = texture(textureSampler, vertTextureCoord);
}

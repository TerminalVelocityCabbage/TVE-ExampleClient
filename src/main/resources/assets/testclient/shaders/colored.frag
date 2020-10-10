#version 330 core

in vec4 vertColor;
in vec2 vertTextureCoord;

out vec4 fragColor;

void main() {
    fragColor = vertColor;
}

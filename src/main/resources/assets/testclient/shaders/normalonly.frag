#version 330 core

in vec3 vertVertexNormal;

out vec4 fragColor;

void main() {
    fragColor = vec4(vertVertexNormal, 1.0);
}

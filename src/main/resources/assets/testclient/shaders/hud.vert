#version 330

layout (location = 0) in vec3 position;

uniform vec4 color;

out vec4 outColor;

void main() {
    gl_Position = vec4(position, 1.0);
    outColor = color;
}

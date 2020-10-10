#version 330 core
layout (location = 0) in vec4 inPosition;
layout (location = 1) in vec4 inColor;

out vec4 vertColor;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
    gl_Position = projectionMatrix * modelViewMatrix * inPosition;
    vertColor = inColor;
}

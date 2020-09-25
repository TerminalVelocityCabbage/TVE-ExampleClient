#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

out vec4 ourColor; // to be sent to frag shader

void main()
{
   gl_Position = vec4(aPos, 1.0);
   ourColor = aColor;
}

#version 330 core

in vec4 ourColor;
in vec2 TexCoord;
out vec4 fragColor;

uniform sampler2D ourTexture;

void main()
{
   //fragColor = texture(ourTexture, TexCoord) * vec4(ourColor);
   fragColor = ourColor;
}

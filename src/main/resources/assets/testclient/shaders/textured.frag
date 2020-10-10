#version 330 core

in vec4 vertColor;
in vec2 vertTextureCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;

float near = 0.1;
float far = 5;

float LinearizeDepth(float depth)
{
   float z = depth * 2.0 - 1.0; // back to NDC
   return (2.0 * near * far) / (far + near - z * (far - near));
}

void main() {
   fragColor = texture(textureSampler, vertTextureCoord) * vertColor;
   //float depth = LinearizeDepth(gl_FragCoord.z) / far;
   //fragColor = vec4(vec3(depth), 1.0);
}

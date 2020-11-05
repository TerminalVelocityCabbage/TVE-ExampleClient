#version 330 core

#include "terminalvelocitycabbage:materials.frag";
#include "terminalvelocitycabbage:lights_base.frag";
#include "terminalvelocitycabbage:point_lights.frag";
#include "terminalvelocitycabbage:spot_lights.frag";
#include "terminalvelocitycabbage:directional_lights.frag";

in vec4 vertColor;
in vec2 vertTextureCoord;
in vec3 vertVertexNormal;
in vec3 vertVertexPosition;

out vec4 fragColor;

const int MAX_POINT_LIGHTS = 256;
const int MAX_SPOT_LIGHTS = 256;

uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform DirectionalLight directionalLight;

void main() {
   setupReflectivity(material, vertTextureCoord);
   setupColors(material, vertTextureCoord);
   //the color of the fragment multiplied by the ambient light
   vec4 color = materialAmbientColor * vec4(ambientLight, 1);
   color += calcDirectionalLight(directionalLight, vertVertexPosition, vertVertexNormal);
   for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
      if (pointLights[i].intensity > 0) {
         color += calcPointLight(pointLights[i], vertVertexPosition, vertVertexNormal);
      }
   }
   for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
      if (spotLights[i].pointLight.intensity > 0) {
         color += calcSpotLight(spotLights[i], vertVertexPosition, vertVertexNormal);
      }
   }
   fragColor = color;
}

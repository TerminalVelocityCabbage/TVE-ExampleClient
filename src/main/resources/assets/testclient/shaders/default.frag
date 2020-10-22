#version 330 core

in vec4 vertColor;
in vec2 vertTextureCoord;
in vec3 vertVertexNormal;
in vec3 vertVertexPosition;

out vec4 fragColor;

struct Attenuation {
   float constant;
   float linear;
   float exponential;
};

struct PointLight {
   vec3 color;
   vec3 position;
   float intensity;
   Attenuation attenuation;
};

struct Material {
   vec4 ambient;
   vec4 diffuse;
   vec4 specular;
   int hasTexture;
   float reflectivity;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;

vec4 ambientColor;
vec4 diffuseColor;
vec4 specularColor;

void setupColors(Material material, vec2 textCoord) {
   if (material.hasTexture == 1) {
      ambientColor = texture(textureSampler, textCoord);
      diffuseColor = ambientColor;
      specularColor = ambientColor;
   } else {
      ambientColor = material.ambient;
      diffuseColor = material.diffuse;
      specularColor = material.specular;
   }
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
   // Diffuse Light
   vec4 dC = vec4(0, 0, 0, 0);
   vec3 lightDirection = light.position - position;
   vec3 toLightSource  = normalize(lightDirection);
   float diffuseFactor = max(dot(normal, toLightSource), 0.0);
   dC = diffuseColor * vec4(light.color, 1.0) * light.intensity * diffuseFactor;

   // Specular Light
   vec4 sC = vec4(0, 0, 0, 0);
   vec3 cameraDirection = normalize(-position);
   vec3 fromLightSource = -toLightSource;
   vec3 reflectedLight = normalize(reflect(fromLightSource, normal));
   float specularFactor = max( dot(cameraDirection, reflectedLight), 0.0);
   specularFactor = pow(specularFactor, specularPower);
   sC = specularColor * specularFactor * material.reflectivity * vec4(light.color, 1.0);

   // Attenuation
   float distance = length(lightDirection);
   float attenuationInv = light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponential * distance * distance);
   return (dC + sC) / attenuationInv;
}

void main() {
   setupColors(material, vertTextureCoord);
   fragColor = ambientColor * vec4(ambientLight, 1) + calcPointLight(pointLight, vertVertexPosition,  vertVertexNormal);
}

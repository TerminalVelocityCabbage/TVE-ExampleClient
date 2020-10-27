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
   vec4 color;
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
uniform vec3 cameraDirection;

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

   //Some inital setup
   vec3 lightDirection = light.position - position;
   float distance = length(lightDirection);
   vec3 cd = normalize(cameraDirection);

   //Unit vector of the direction towards a light
   vec3 unitDirectionTowardsLight  = normalize(lightDirection);

   //The ammount of light reflected towards the camera from the light source
   vec3 reflectedLight = normalize(reflect(-unitDirectionTowardsLight, normal));

   //how much light 0-1 a vertex gets.
   //If normal vector is opposite to vector coming from light 100% light
   //If normal vector is the same as the vector coming from the light 0% light
   float diffuseFactor = max(dot(normal, unitDirectionTowardsLight), 0.0);
   vec4 finalDiffuseColor = diffuseColor * light.color * diffuseFactor;

   // Specular Light
   //A value from 0 to 1 that represents the ammount of light reflected into the camera
   float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
   specularFactor = pow(specularFactor, specularPower);
   vec4 finalSpecularColor = specularColor * specularFactor * material.reflectivity;

   //Attenuation - the higer the number here the less light will make it to an objects
   float attenuationFade = 1 / (light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponential * (distance * distance)));

   return light.intensity * attenuationFade * (vec4(finalDiffuseColor) + finalSpecularColor);
}

void main() {
   setupColors(material, vertTextureCoord);
   vec4 unlitColor = ambientColor * vec4(ambientLight, 1);
   fragColor = unlitColor + calcPointLight(pointLight, vertVertexPosition, vertVertexNormal);
}

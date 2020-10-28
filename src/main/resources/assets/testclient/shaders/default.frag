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

struct SpotLight {
   PointLight pointLight;
   vec3 coneDirection;
   float cutoff;
};

struct DirectionalLight {
   vec3 direction;
   vec4 color;
   float intensity;
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
uniform SpotLight spotLight;
uniform DirectionalLight directionalLight;

vec4 materialAmbientColor;
vec4 materialDiffuseColor;
vec4 materialSpecularColor;

void setupColors(Material material, vec2 textCoord) {
   if (material.hasTexture == 1) {
      materialAmbientColor = texture(textureSampler, textCoord);
      materialDiffuseColor = materialAmbientColor;
      materialSpecularColor = materialAmbientColor;
   } else {
      materialAmbientColor = material.ambient;
      materialDiffuseColor = material.diffuse;
      materialSpecularColor = material.specular;
   }
}

vec4 calcLightColor(vec4 lightColor, float lightIntensity, vec3 position, vec3 toLightDirection, vec3 normal) {

   //Setup
   vec3 cameraDirection = normalize(-position);
   vec3 fromLightDirection = -toLightDirection;
   vec3 reflectedLight = normalize(reflect(fromLightDirection, normal));

   //Diffuse
   vec4 diffuseColor = vec4(0, 0, 0, 0);
   float diffuseFactor = max(dot(normal, toLightDirection), 0.0);
   diffuseColor = materialDiffuseColor * lightColor * lightIntensity * diffuseFactor;

   //Specular
   vec4 specularColor = vec4(0, 0, 0, 0);
   float specularFactor = pow(max(dot(cameraDirection, reflectedLight), 0.0), specularPower);
   specularColor = materialSpecularColor * lightIntensity * specularFactor * material.reflectivity * lightColor;

   return (diffuseColor + specularColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {

   //Some inital setup
   vec3 lightDirection = light.position - position;
   vec3 unitToLightDirection  = normalize(lightDirection);
   vec4 lightColor = calcLightColor(light.color, light.intensity, position, unitToLightDirection, normal);
   float distance = length(lightDirection);

   //Attenuation (fade of light by distance)
   float attenuationFade = light.attenuation.constant + (light.attenuation.linear * distance) + (light.attenuation.exponential * (distance * distance));

   return lightColor / attenuationFade;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal) {

   //Some initial setup
   vec3 lightDirection = light.pointLight.position - position;
   vec3 unitFromLightDirection = -normalize(lightDirection);
   //Get the angle of the vertex to the light source direction to determine later if it's within the cone angle
   float spotAlpha = dot(unitFromLightDirection, normalize(light.coneDirection));

   vec4 color = vec4(0, 0, 0, 0);
   if (spotAlpha > light.cutoff) {
      color = calcPointLight(light.pointLight, position, normal);
      //Determine the percentage of light getting to a fragment based on the distance from the cone direction vector
      color *= (1.0 - (1.0 - spotAlpha) / (1.0 - light.cutoff));
   }

   return color;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
   return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main() {
   setupColors(material, vertTextureCoord);
   //the color of the fragment multiplied by the ambient light
   vec4 color = materialAmbientColor * vec4(ambientLight, 1);
   color += calcPointLight(pointLight, vertVertexPosition, vertVertexNormal);
   color += calcDirectionalLight(directionalLight, vertVertexPosition, vertVertexNormal);
   color += calcSpotLight(spotLight, vertVertexPosition, vertVertexNormal);
   fragColor = color;
}

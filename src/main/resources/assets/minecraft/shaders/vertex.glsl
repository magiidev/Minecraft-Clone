#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoords;
layout(location = 2) in vec3 normals;

out vec2 fragTexCoords;
out vec3 fragNormals;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main() {
    // Calcul de la position finale
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);

    // Transmission des coordonnées de texture au fragment shader
    fragTexCoords = texCoords;
    fragNormals = normals;
}

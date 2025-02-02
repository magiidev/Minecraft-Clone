#version 330 core

in vec2 fragTexCoords;
in vec3 fragNormals;
out vec4 fragColor;

uniform sampler2D sideTexture;
uniform sampler2D topTexture;
uniform sampler2D bottomTexture;

uniform bool usingDifferentTextures;

void main() {
    if (usingDifferentTextures) {
        // Supposons que fragTexCoords.y détermine le haut/bas/côtés
        if (fragNormals.y > 0) {
            // Top (texture du dessus)
            fragColor = texture(topTexture, fragTexCoords);
        } else if (fragNormals.y < 0) {
            // Bottom (texture du dessous)
            fragColor = texture(bottomTexture, fragTexCoords);
        } else {
            // Sides (texture par défaut)
            fragColor = texture(sideTexture, fragTexCoords);
        }
    } else {
        // Si une seule texture est utilisée pour toutes les faces
        fragColor = texture(sideTexture, fragTexCoords);
    }
}

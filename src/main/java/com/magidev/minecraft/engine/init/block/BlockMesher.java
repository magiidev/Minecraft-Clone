package com.magidev.minecraft.engine.init.block;

import com.magidev.minecraft.engine.rendering.objects.Model;

public class BlockMesher {
    public static float[] vertices = {
            -0.5f,-0.5f,0.5f, 0.5f,-0.5f,0.5f, 0.5f,0.5f,0.5f, -0.5f,0.5f,0.5f,
            -0.5f,-0.5f,-0.5f, 0.5f,-0.5f,-0.5f, 0.5f,0.5f,-0.5f, -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f, -0.5f,-0.5f,0.5f, -0.5f,0.5f,0.5f, -0.5f,0.5f,-0.5f,
            0.5f,-0.5f,0.5f, 0.5f,-0.5f,-0.5f, 0.5f,0.5f,-0.5f, 0.5f,0.5f,0.5f,
            -0.5f,0.5f,0.5f, 0.5f,0.5f,0.5f, 0.5f,0.5f,-0.5f, -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f, 0.5f,-0.5f,-0.5f, 0.5f,-0.5f,0.5f, -0.5f,-0.5f,0.5f
    };

    public static float[] textureCoords = {
            0.0f,0.0f, 1.0f,0.0f, 1.0f,1.0f, 0.0f,1.0f,
            0.0f,0.0f, 1.0f,0.0f, 1.0f,1.0f, 0.0f,1.0f,
            0.0f,0.0f, 1.0f,0.0f, 1.0f,1.0f, 0.0f,1.0f,
            0.0f,0.0f, 1.0f,0.0f, 1.0f,1.0f, 0.0f,1.0f,
            0.0f,0.0f, 1.0f,0.0f, 1.0f,1.0f, 0.0f,1.0f,
            0.0f,0.0f, 1.0f,0.0f, 1.0f,1.0f, 0.0f,1.0f
    };

    public static int[] indices = {
            0,1,2, 2,3,0, 4,5,6, 6,7,4, 8,9,10, 10,11,8,
            12,13,14, 14,15,12, 16,17,18, 18,19,16, 20,21,22, 22,23,20
    };

    public static float[] normals = {
            // Face avant
            0.0f, 0.0f, 1.0f,  // Normale vers la face avant
            0.0f, 0.0f, 1.0f,  // Normale vers la face avant
            0.0f, 0.0f, 1.0f,  // Normale vers la face avant
            0.0f, 0.0f, 1.0f,  // Normale vers la face avant

            // Face arrière
            0.0f, 0.0f, -1.0f, // Normale vers la face arrière
            0.0f, 0.0f, -1.0f, // Normale vers la face arrière
            0.0f, 0.0f, -1.0f, // Normale vers la face arrière
            0.0f, 0.0f, -1.0f, // Normale vers la face arrière

            // Face gauche
            -1.0f, 0.0f, 0.0f, // Normale vers la face gauche
            -1.0f, 0.0f, 0.0f, // Normale vers la face gauche
            -1.0f, 0.0f, 0.0f, // Normale vers la face gauche
            -1.0f, 0.0f, 0.0f, // Normale vers la face gauche

            // Face droite
            1.0f, 0.0f, 0.0f,  // Normale vers la face droite
            1.0f, 0.0f, 0.0f,  // Normale vers la face droite
            1.0f, 0.0f, 0.0f,  // Normale vers la face droite
            1.0f, 0.0f, 0.0f,  // Normale vers la face droite

            // Face supérieure
            0.0f, 1.0f, 0.0f,  // Normale vers la face supérieure
            0.0f, 1.0f, 0.0f,  // Normale vers la face supérieure
            0.0f, 1.0f, 0.0f,  // Normale vers la face supérieure
            0.0f, 1.0f, 0.0f,  // Normale vers la face supérieure

            // Face inférieure
            0.0f, -1.0f, 0.0f, // Normale vers la face inférieure
            0.0f, -1.0f, 0.0f, // Normale vers la face inférieure
            0.0f, -1.0f, 0.0f, // Normale vers la face inférieure
            0.0f, -1.0f, 0.0f  // Normale vers la face inférieure
    };

    public static Model createBlockModel() {
        return new Model(vertices, textureCoords, normals, indices);
    }

    public static Model createBlockSlabModel() {
        float[] slabVertices = {
                -0.5f,-0.5f,0.5f, 0.5f,-0.5f,0.5f, 0.5f,0.0f,0.5f, -0.5f,0.0f,0.5f,
                -0.5f,-0.5f,-0.5f, 0.5f,-0.5f,-0.5f, 0.5f,0.0f,-0.5f, -0.5f,0.0f,-0.5f,
                -0.5f,-0.5f,-0.5f, -0.5f,-0.5f,0.5f, -0.5f,0.0f,0.5f, -0.5f,0.0f,-0.5f,
                0.5f,-0.5f,0.5f, 0.5f,-0.5f,-0.5f, 0.5f,0.0f,-0.5f, 0.5f,0.0f,0.5f,
                -0.5f,0.0f,0.5f, 0.5f,0.0f,0.5f, 0.5f,0.0f,-0.5f, -0.5f,0.0f,-0.5f,
                -0.5f,-0.5f,-0.5f, 0.5f,-0.5f,-0.5f, 0.5f,-0.5f,0.5f, -0.5f,-0.5f,0.5f
        };
        return new Model(slabVertices, textureCoords, null, indices);
    }
}

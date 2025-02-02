package com.magidev.minecraft.engine.rendering.objects;



import com.magidev.minecraft.Minecraft;

import com.magidev.minecraft.engine.rendering.shaders.StaticShader;

import com.magidev.minecraft.engine.rendering.textures.Texture;

import org.joml.Matrix4f;

import org.lwjgl.system.MemoryUtil;



import java.nio.FloatBuffer;

import java.nio.IntBuffer;

import java.util.ArrayList;

import java.util.List;



import static org.lwjgl.opengl.GL40.*;



public class Model {



    private final int vaoId;

    private final List<Integer> vboIds;

    private final int vertexCount;



    private Texture textureDefault; // Texture principale

    private Texture textureTop;    // Texture pour la partie supérieure

    private Texture textureBottom; // Texture pour la partie inférieure



    // Transformations locales

    private final Matrix4f modelMatrix;

    private final org.joml.Vector3f position;

    private final org.joml.Vector3f rotation;

    private final org.joml.Vector3f scale;



    public Model(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {

        // Générer le VAO

        vaoId = glGenVertexArrays();

        glBindVertexArray(vaoId);



        vboIds = new ArrayList<>();



        // Charger les données des vertices

        vboIds.add(createVBO(0, 3, vertices));



        // Charger les coordonnées de texture

        vboIds.add(createVBO(1, 2, textureCoords));



        // Charger les normales si elles sont fournies

        if (normals != null) {

            vboIds.add(createVBO(2, 3, normals));

        }



        // Charger les indices

        int eboId = createEBO(indices);

        vboIds.add(eboId);



        vertexCount = indices.length;



        // Initialiser les transformations

        modelMatrix = new Matrix4f();

        position = new org.joml.Vector3f(0, 0, 0);

        rotation = new org.joml.Vector3f(0, 0, 0);

        scale = new org.joml.Vector3f(1, 1, 1);



        // Désactiver le VAO

        glBindVertexArray(0);

    }



    public void setSideTexture(Texture textureDefault) {

        this.textureDefault = textureDefault;

    }



    public void setTopTexture(Texture textureTop) {

        this.textureTop = textureTop;

    }



    public void setBottomTexture(Texture textureBottom) {

        this.textureBottom = textureBottom;

    }



    public void setPosition(float x, float y, float z) {

        position.set(x, y, z);

    }



    public void setRotation(float x, float y, float z) {

        rotation.set(x, y, z);

    }



    public void setScale(float x, float y, float z) {

        scale.set(x, y, z);

    }



    public Matrix4f getModelMatrix() {

        modelMatrix.identity()

                .translate(position)

                .rotateX((float) Math.toRadians(rotation.x))

                .rotateY((float) Math.toRadians(rotation.y))

                .rotateZ((float) Math.toRadians(rotation.z))

                .scale(scale);

        return modelMatrix;

    }



    private int createVBO(int index, int size, float[] data) {

        int vboId = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vboId);



        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);

        buffer.put(data).flip();



        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);

        glEnableVertexAttribArray(index);



        MemoryUtil.memFree(buffer);

        return vboId;

    }



    private int createEBO(int[] indices) {

        int eboId = glGenBuffers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);



        IntBuffer buffer = MemoryUtil.memAllocInt(indices.length);

        buffer.put(indices).flip();



        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);



        MemoryUtil.memFree(buffer);

        return eboId;

    }



    public void render(boolean usingDifferentTextures) {

        StaticShader staticShader = Minecraft.getStaticShader();

        staticShader.updateTransformation(getModelMatrix());



        if (usingDifferentTextures) {

            // Si on utilise des textures différentes

            if (textureTop != null) {

                textureTop.bind(1); // Bind la textureTop sur l'unité 1

            }



            if (textureBottom != null) {

                textureBottom.bind(2); // Bind la textureBottom sur l'unité 2

            }

        } else {

            // Sinon, on utilise la texture principale

            if (textureDefault != null) {

                textureDefault.bind(0); // Bind la texture principale sur l'unité 0

            } else {

                glBindTexture(GL_TEXTURE_2D, 0); // Pas de texture

            }

        }



        // Rendu du modèle

        glBindVertexArray(vaoId);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);

    }



    public void cleanup() {

        if (textureDefault != null) {

            textureDefault.cleanup();

        }

        if (textureTop != null) {

            textureTop.cleanup();

        }

        if (textureBottom != null) {

            textureBottom.cleanup();

        }



        // Supprimer les VBO

        for (int vboId : vboIds) {

            glDeleteBuffers(vboId);

        }

        // Supprimer le VAO

        glDeleteVertexArrays(vaoId);

    }



}
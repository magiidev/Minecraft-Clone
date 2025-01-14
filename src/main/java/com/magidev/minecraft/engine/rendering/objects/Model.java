package com.magidev.minecraft.engine.rendering.objects;

import com.magidev.minecraft.engine.rendering.textures.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL40.*;

public class Model {

    private final int vaoId;
    private final List<Integer> vboIds;
    private final int vertexCount;

    private Texture texture; // Texture optionnelle

    public Model(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
        // Générer le VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboIds = new ArrayList<>();

        // Charger les données des vertices
        vboIds.add(createVBO(0, 3, vertices));

        // Charger les coordonnées de texture
        vboIds.add(createVBO(1, 2, textureCoords));

        // Charger les normales (optionnel)
        if (normals != null) {
            vboIds.add(createVBO(2, 3, normals));
        }

        // Charger les indices
        int eboId = createEBO(indices);
        vboIds.add(eboId);

        vertexCount = indices.length;

        // Désactiver le VAO
        glBindVertexArray(0);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
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

    public void render()
    {
        // Lier la texture si elle est définie
        if (texture != null) {
            texture.bind(0);
        } else {
            glBindTexture(GL_TEXTURE_2D, 0); // Pas de texture
        }
        
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void cleanup() {
        if (texture != null) {
            texture.cleanup();
        }

        // Supprimer les VBO
        for (int vboId : vboIds) {
            glDeleteBuffers(vboId);
        }
        // Supprimer le VAO
        glDeleteVertexArrays(vaoId);
    }
}

 package com.magidev.minecraft.engine.rendering.shaders;

import com.magidev.minecraft.engine.utils.Utils;

import org.joml.Matrix4f;

public class StaticShader extends Shader
{

    private final Matrix4f transformationMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f projectionMatrix;

    public StaticShader()
    {
        // Initialisation des matrices
        transformationMatrix = new Matrix4f().identity();
        viewMatrix = new Matrix4f().identity();
        projectionMatrix = new Matrix4f().identity();


        // Initialisation des shaders
        createVertexShader(Utils.loadShaderSource("vertex.glsl"));
        createFragmentShader(Utils.loadShaderSource("fragment.glsl"));
        link();
    }

    @Override
    public void createUniforms() {
        createUniform("sideTexture");
        createUniform("topTexture");
        createUniform("bottomTexture");
        createUniform("usingDifferentTextures");
        createUniform("transformationMatrix");
        createUniform("viewMatrix");
        createUniform("projectionMatrix");
    }

    @Override
    public void setUniforms()
    {
        setUniform("sideTexture", 0);
        setUniform("topTexture", 1);
        setUniform("bottomTexture", 2);
        setUniform("transformationMatrix", transformationMatrix); // Envoi de la matrice de transformation
        setUniform("viewMatrix", viewMatrix);          // Envoi de la matrice de vue
        setUniform("projectionMatrix", projectionMatrix); // Envoi de la matrice de projection
    }

    public void setUsingDifferentTextures(boolean value) {
        setUniform("usingDifferentTextures", value);
    }

    public void updateTransformation(Matrix4f transformation) {
        transformationMatrix.set(transformation); // Mise à jour de la matrice de transformation
    }

    public void updateView(Matrix4f view) {
        viewMatrix.set(view); // Mise à jour de la matrice de vue
    }

    public void updateProjection(Matrix4f projection) {
        projectionMatrix.set(projection); // Mise à jour de la matrice de projection
    }

    public Matrix4f getTransformationMatrix() {
        return transformationMatrix;
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

}
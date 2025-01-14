package com.magidev.minecraft.engine.rendering.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.Map;

public class ShaderProgram {
    private final int programId;
    private final Map<String, Integer> uniforms;

    public ShaderProgram() {
        programId = GL20.glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Impossible de créer un programme shader");
        }
        uniforms = new HashMap<>();
    }

    public void createVertexShader(String shaderCode) {
        createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) {
        createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    private void createShader(String shaderCode, int shaderType) {
        int shaderId = GL20.glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Impossible de créer un shader, type: " + shaderType);
        }

        GL20.glShaderSource(shaderId, shaderCode);
        GL20.glCompileShader(shaderId);

        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Erreur de compilation du shader : " + GL20.glGetShaderInfoLog(shaderId));
        }

        GL20.glAttachShader(programId, shaderId);
    }

    public void link() {
        GL20.glLinkProgram(programId);

        if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Erreur de liaison du programme shader : " + GL20.glGetProgramInfoLog(programId));
        }

        GL20.glValidateProgram(programId);
    }

    public void createUniform(String uniformName) {
        int uniformLocation = GL20.glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new RuntimeException("Impossible de trouver l'uniform : " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, float value) {
        GL20.glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, int value) {
        GL20.glUniform1i(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, float x, float y, float z) {
        GL20.glUniform3f(uniforms.get(uniformName), x, y, z);
    }

    public void bind() {
        GL20.glUseProgram(programId);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            GL20.glDeleteProgram(programId);
        }
    }
}

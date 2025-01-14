package com.magidev.minecraft;

import com.magidev.minecraft.engine.logic.GameLogic;
import com.magidev.minecraft.engine.rendering.objects.Model;
import com.magidev.minecraft.engine.rendering.shaders.ShaderProgram;
import com.magidev.minecraft.engine.rendering.textures.Texture;
import com.magidev.minecraft.engine.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL40.*;

public class Minecraft implements GameLogic {
    private static Model model;
    private static ShaderProgram shaderProgram;

    public Minecraft() {}

    @Override
    public void init() {
        // Initialisation des shaders
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadShaderSource("vertex.glsl"));
        shaderProgram.createFragmentShader(Utils.loadShaderSource("fragment.glsl"));
        shaderProgram.link();

        float[] vertices = {
                // Positions (x, y, z)
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f
        };

        float[] textureCoords = {
                // UV (u, v)
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        int[] indices = {
                0, 1, 2,
                2, 3, 0
        };

        model = new Model(vertices, textureCoords, null, indices);
        model.setTexture(new Texture(Utils.getResource("/assets/minecraft/textures/blocks/grass.png")));

        // OpenGL settings
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    }

    @Override
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shaderProgram.createUniform("textureSampler");
        shaderProgram.bind();
        shaderProgram.setUniform("textureSampler", 0); // Texture unit 0
        model.render();
        shaderProgram.unbind();
    }

    @Override
    public void input(double TICK_RATE) {}

    @Override
    public void update(double TICK_RATE) {}

    @Override
    public void cleanup() {
        model.cleanup();
        shaderProgram.cleanup();
    }
}

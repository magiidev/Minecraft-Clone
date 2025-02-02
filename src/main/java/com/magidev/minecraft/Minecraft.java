package com.magidev.minecraft;

import com.magidev.minecraft.engine.controllers.Mouse;
import com.magidev.minecraft.engine.controllers.NoClipCamera;
import com.magidev.minecraft.engine.init.block.Block;
import com.magidev.minecraft.engine.logic.GameLogic;
import com.magidev.minecraft.engine.rendering.objects.Model;
import com.magidev.minecraft.engine.rendering.shaders.Shader;
import com.magidev.minecraft.engine.rendering.shaders.StaticShader;
import com.magidev.minecraft.engine.rendering.textures.Texture;
import com.magidev.minecraft.engine.utils.Utils;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL40.*;

public class Minecraft implements GameLogic
{
    private NoClipCamera camera;
    private float rotationAngle = 0.0f; // Angle de rotation

    private static Block block;
    private static StaticShader staticShader;

    public Minecraft()
    {
        staticShader = new StaticShader();
        camera = new NoClipCamera(10.0f, 0.1f);
    }

    @Override
    public void init() {
        Matrix4f projection = staticShader.getProjectionMatrix();
        projection.setPerspective((float) Math.toRadians(70.0f), 16.0f / 9.0f, 0.01f, 1000.0f);

        Matrix4f view = staticShader.getViewMatrix();
        view.identity()
                .lookAt(0.0f, 0.0f, 3.0f,
                        0.0f, 0.0f, 0.0f,
                        0.0f, 1.0f, 0.0f);

        staticShader.updateProjection(projection);
        staticShader.updateView(view);

        block = new Block(0, "dirt");

        // OpenGL settings
        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    }

    @Override
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        staticShader.bind();
        block.render();
        staticShader.unbind();
    }

    @Override
    public void input(double TICK_RATE) {}

    @Override
    public void update(double deltaTime)
    {
        Mouse.init(Main.getWindow().getWindowHandle());

        // Mise à jour de la caméra avec deltaTime
        camera.input((float) deltaTime);
        staticShader.updateView(camera.getViewMatrix());

        // Rotation du modèle
        rotationAngle += (float) (35f * deltaTime);
        block.getModel().setRotation(0, rotationAngle, 0);
    }

    @Override
    public void cleanup() {
        block.cleanup();
        staticShader.cleanup();
    }

    public static StaticShader getStaticShader()
    {
        return staticShader;
    }
}

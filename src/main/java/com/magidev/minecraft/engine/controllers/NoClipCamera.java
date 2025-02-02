package com.magidev.minecraft.engine.controllers;

import com.magidev.minecraft.Main;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class NoClipCamera {
    private final Vector3f position;
    private final Vector3f rotation;
    private final float moveSpeed;
    private final float rotationSpeed;

    public NoClipCamera(float moveSpeed, float rotationSpeed) {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.moveSpeed = moveSpeed;
        this.rotationSpeed = rotationSpeed;
    }

    public Matrix4f getViewMatrix() {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity()
                .rotate((float) Math.toRadians(rotation.x), 1, 0, 0)
                .rotate((float) Math.toRadians(rotation.y), 0, 1, 0)
                .translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }

    // Déplacement vers l'avant
    public void moveForward(float deltaTime) {
        float delta = moveSpeed * deltaTime;

        // Calculer la direction de la caméra
        float radYaw = (float) Math.toRadians(rotation.y);
        float sinYaw = (float) Math.sin(radYaw);
        float cosYaw = (float) Math.cos(radYaw);

        // Déplacement dans la direction de la caméra
        position.x += sinYaw * delta;
        position.z -= cosYaw * delta;
    }

    // Déplacement vers l'arrière
    public void moveBackward(float deltaTime) {
        float delta = moveSpeed * deltaTime;

        float radYaw = (float) Math.toRadians(rotation.y);
        float sinYaw = (float) Math.sin(radYaw);
        float cosYaw = (float) Math.cos(radYaw);

        position.x -= sinYaw * delta;
        position.z += cosYaw * delta;
    }

    // Déplacement latéral à gauche
    public void strafeLeft(float deltaTime) {
        float delta = moveSpeed * deltaTime;

        float radYaw = (float) Math.toRadians(rotation.y);
        float sinYaw = (float) Math.sin(radYaw);
        float cosYaw = (float) Math.cos(radYaw);

        // Strafe à gauche (90° dans le sens antihoraire)
        position.x -= cosYaw * delta;
        position.z -= sinYaw * delta;
    }

    // Déplacement latéral à droite
    public void strafeRight(float deltaTime) {
        float delta = moveSpeed * deltaTime;

        float radYaw = (float) Math.toRadians(rotation.y);
        float sinYaw = (float) Math.sin(radYaw);
        float cosYaw = (float) Math.cos(radYaw);

        // Strafe à droite (90° dans le sens horaire)
        position.x += cosYaw * delta;
        position.z += sinYaw * delta;
    }

    // Déplacement vertical
    public void moveUp(float deltaTime) {
        position.y += moveSpeed * deltaTime;
    }

    public void moveDown(float deltaTime) {
        position.y -= moveSpeed * deltaTime;
    }

    // Rotation basée sur la souris
    public void rotate(float dx, float dy) {
        rotation.x += dy * rotationSpeed;
        rotation.y += dx * rotationSpeed;

        // Limiter les angles de rotation pour éviter les anomalies
        rotation.x = Math.max(-90, Math.min(90, rotation.x));
    }

    // Gestion des entrées (clavier et souris)
    public void input(float deltaTime) {
        // Gestion des déplacements via les touches
        if (glfwGetKey(Main.getWindow().getWindowHandle(), GLFW_KEY_W) == GLFW_PRESS) {
            moveForward(deltaTime);
        }
        if (glfwGetKey(Main.getWindow().getWindowHandle(), GLFW_KEY_S) == GLFW_PRESS) {
            moveBackward(deltaTime);
        }
        if (glfwGetKey(Main.getWindow().getWindowHandle(), GLFW_KEY_A) == GLFW_PRESS) {
            strafeLeft(deltaTime);
        }
        if (glfwGetKey(Main.getWindow().getWindowHandle(), GLFW_KEY_D) == GLFW_PRESS) {
            strafeRight(deltaTime);
        }
        if (glfwGetKey(Main.getWindow().getWindowHandle(), GLFW_KEY_SPACE) == GLFW_PRESS) {
            moveUp(deltaTime);
        }
        if (glfwGetKey(Main.getWindow().getWindowHandle(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            moveDown(deltaTime);
        }

        // Gestion de la rotation via la souris
        float dx = Mouse.getDeltaX();
        float dy = Mouse.getDeltaY();
        rotate(dx, dy); // Utilisation de la souris pour la rotation de la caméra
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}

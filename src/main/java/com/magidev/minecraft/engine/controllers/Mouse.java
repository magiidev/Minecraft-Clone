package com.magidev.minecraft.engine.controllers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Mouse {
    private static double previousX = 0;
    private static double previousY = 0;
    private static double deltaX = 0;
    private static double deltaY = 0;
    private static boolean firstMouse = true;

    private static boolean cursorLocked = true;

    public static void init(long window) {
        GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                if (cursorLocked) {
                    if (firstMouse) {
                        previousX = xpos;
                        previousY = ypos;
                        firstMouse = false;
                    }

                    deltaX = xpos - previousX; // Inversion de l'axe horizontal
                    deltaY = ypos - previousY; // Inversion de l'axe vertical

                    previousX = xpos;
                    previousY = ypos;
                }
            }
        });

        // Masquer le curseur et le verrouiller au centre de la fenêtre
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public static void toggleCursor(long window) {
        cursorLocked = !cursorLocked;

        if (cursorLocked) {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            firstMouse = true; // Réinitialiser pour éviter un grand saut de la caméra
        } else {
            GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    public static float getDeltaX() {
        float result = (float) deltaX;
        deltaX = 0; // Réinitialiser après avoir récupéré la valeur
        return result;
    }

    public static float getDeltaY() {
        float result = (float) deltaY;
        deltaY = 0; // Réinitialiser après avoir récupéré la valeur
        return result;
    }

    public static boolean isCursorLocked() {
        return cursorLocked;
    }
}

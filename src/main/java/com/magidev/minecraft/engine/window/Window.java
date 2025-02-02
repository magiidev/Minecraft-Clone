package com.magidev.minecraft.engine.window;

import com.magidev.minecraft.engine.controllers.Keyboard;
import com.magidev.minecraft.engine.utils.Utils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window
{
    private static Keyboard keyboard = new Keyboard();  // <- ❗️ Create a Keyboard instance

    private static long window;

    private String title;
    private int width, height;
    private boolean vSync;

    private boolean captured = true; // L'état initial est "captured"
    private boolean f1Pressed = false; // Pour éviter de basculer plusieurs fois lorsque F1 est maintenue

    private boolean fullscreen = false; // État initial : pas en plein écran
    private int windowedWidth, windowedHeight; // Dimensions en mode fenêtré
    private int windowedPosX, windowedPosY; // Position en mode fenêtré
    private boolean f11Pressed = false; // Pour éviter de basculer plusieurs fois lorsque F11 est maintenue


    public Window(String title, int width, int height, boolean vSync)
    {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    public void init()
    {
        initGLFW();

        // Créer une fenêtre avec les dimensions spécifiées (sans plein écran)
        window = glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Ajouter le callback pour le clavier via la classe Keyboard (qui est déjà définie dans ton code)
        glfwSetKeyCallback(window, new Keyboard()); // Utilise la classe Keyboard pour gérer les entrées du clavier

        // Récupérer la taille de la fenêtre et la positionner au centre de l'écran
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);

            // Récupérer la résolution du moniteur principal pour centrer la fenêtre
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Activer la synchronisation verticale (V-Sync)
        if (vSync) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }

        // Rendre la fenêtre visible
        glfwShowWindow(window);

        // Cette ligne est cruciale pour l'interopération de LWJGL avec le contexte OpenGL de GLFW.
        GL.createCapabilities();
    }


    public static void initGLFW()
    {
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
    }

    public void update()
    {
        glfwPollEvents();
        glfwSwapBuffers(window);

        if (keyboard.isKeyDown(GLFW_KEY_ESCAPE))
            glfwSetWindowShouldClose(window, true);

        if (glfwGetKey(window, GLFW_KEY_F11) == GLFW_PRESS)
        {
            if (!f11Pressed) {
                toggleFullscreen();
                f11Pressed = true;
            }
        } else {
            f11Pressed = false;
        }


        if (glfwGetKey(window, GLFW_KEY_F1) == GLFW_PRESS)
        {
            if (!f1Pressed)
            {
                captured = !captured;
                f1Pressed = true;
            }
        } else {
            f1Pressed = false;
        }
    }

    private void toggleFullscreen()
    {
        fullscreen = !fullscreen;

        if (fullscreen)
        {
            // Sauvegarder les dimensions et la position actuelles de la fenêtre
            try (MemoryStack stack = stackPush())
            {
                IntBuffer pWidth = stack.mallocInt(1);
                IntBuffer pHeight = stack.mallocInt(1);
                IntBuffer pX = stack.mallocInt(1);
                IntBuffer pY = stack.mallocInt(1);

                glfwGetWindowSize(window, pWidth, pHeight);
                glfwGetWindowPos(window, pX, pY);

                windowedWidth = pWidth.get(0);
                windowedHeight = pHeight.get(0);
                windowedPosX = pX.get(0);
                windowedPosY = pY.get(0);
            }

            // Passer en plein écran
            long monitor = glfwGetPrimaryMonitor();
            GLFWVidMode vidmode = glfwGetVideoMode(monitor);

            glfwSetWindowMonitor(window, monitor, 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
        } else {
            // Revenir en mode fenêtré
            glfwSetWindowMonitor(window, NULL, windowedPosX, windowedPosY, windowedWidth, windowedHeight, 0);
        }
    }

    public void setIcon(InputStream iconStream16X, InputStream iconStream32X)
    {
        try (MemoryStack memorystack = MemoryStack.stackPush())
        {
            if (iconStream16X == null)
            {
                throw new FileNotFoundException("icons/icon_16x16.png");
            }

            if (iconStream32X == null)
            {
                throw new FileNotFoundException("icons/icon_32x32.png");
            }

            IntBuffer intbuffer = memorystack.mallocInt(1);
            IntBuffer intbuffer1 = memorystack.mallocInt(1);
            IntBuffer intbuffer2 = memorystack.mallocInt(1);
            GLFWImage.Buffer buffer = GLFWImage.mallocStack(2, memorystack);
            ByteBuffer bytebuffer = this.loadIcon(iconStream16X, intbuffer, intbuffer1, intbuffer2);

            if (bytebuffer == null)
            {
                throw new IllegalStateException("Could not load icon: " + STBImage.stbi_failure_reason());
            }

            buffer.position(0);
            buffer.width(intbuffer.get(0));
            buffer.height(intbuffer1.get(0));
            buffer.pixels(bytebuffer);
            ByteBuffer bytebuffer1 = this.loadIcon(iconStream32X, intbuffer, intbuffer1, intbuffer2);

            if (bytebuffer1 == null)
            {
                throw new IllegalStateException("Could not load icon: " + STBImage.stbi_failure_reason());
            }

            buffer.position(1);
            buffer.width(intbuffer.get(0));
            buffer.height(intbuffer1.get(0));
            buffer.pixels(bytebuffer1);
            buffer.position(0);
            GLFW.glfwSetWindowIcon(window, buffer);
            STBImage.stbi_image_free(bytebuffer);
            STBImage.stbi_image_free(bytebuffer1);
        }
        catch (IOException ioexception1)
        {
            System.out.println("Couldn't set icon");
        }
    }

    @Nullable
    private ByteBuffer loadIcon(InputStream textureStream, IntBuffer x, IntBuffer y, IntBuffer channelInFile) throws IOException
    {
        ByteBuffer bytebuffer = null;
        ByteBuffer bytebuffer1;

        try
        {
            bytebuffer = Utils.readToBuffer(textureStream);
            ((java.nio.Buffer)bytebuffer).rewind();
            bytebuffer1 = STBImage.stbi_load_from_memory(bytebuffer, x, y, channelInFile, 0);
        }
        finally
        {
            if (bytebuffer != null)
            {
                MemoryUtil.memFree(bytebuffer);
            }
        }

        return bytebuffer1;
    }

    public boolean shouldClose()
    {
        return glfwWindowShouldClose(window);
    }

    public void close()
    {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public long getWindow()
    {
        return window;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String newTitle)
    {
        this.title = newTitle;

        glfwSetWindowTitle(window, newTitle);
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public static int getWidth()
    {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, w, null);

        return w.get(0);
    }

    public static int getHeight()
    {
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, null, h);

        return h.get(0);
    }

    public long getWindowHandle()
    {
        return window;
    }
}
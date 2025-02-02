package com.magidev.minecraft.engine.utils;

import com.magidev.minecraft.engine.window.Window;
import org.lwjgl.system.MemoryUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.lwjgl.opengl.GL40.glViewport;


public class Utils
{
    public static void updateViewportIfNeeded(Window window)
    {
        // Récupérer les dimensions actuelles de la fenêtre
        int currentWidth = Window.getWidth();
        int currentHeight = Window.getHeight();

        // Vérifier si les dimensions ont changé
        if (currentWidth != window.getWidth() || currentHeight != window.getHeight())
        {
            // Mettre à jour les dimensions stockées dans la fenêtre
            window.setWidth(currentWidth);
            window.setHeight(currentHeight);

            // Mettre à jour le viewport OpenGL
            glViewport(0, 0, currentWidth, currentHeight);

            System.out.println(currentWidth);
            System.out.println(currentHeight);
        }
    }

    public static boolean fileExists(String path) {
        // Vérification depuis le système de fichiers
        if (Files.exists(Paths.get(path))) {
            return true;
        }

        // Vérification dans le classpath (pour les fichiers dans le JAR)
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(path)) {
            return is != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static String loadShaderSource(String fileName) {
        try (InputStream is = Utils.class.getResourceAsStream("/assets/minecraft/shaders/" + fileName)) {
            if (is == null) {
                throw new RuntimeException("Fichier shader introuvable : " + fileName);
            }
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement du shader : " + fileName, e);
        }
    }

    public static InputStream getResource(String filePath)
    {
        return Utils.class.getResourceAsStream("/assets/minecraft/" + filePath);
    }

    public static ByteBuffer readToBuffer(InputStream inputStreamIn) throws IOException
    {
        ByteBuffer bytebuffer;

        if (inputStreamIn instanceof FileInputStream)
        {
            FileInputStream fileinputstream = (FileInputStream)inputStreamIn;
            FileChannel filechannel = fileinputstream.getChannel();
            bytebuffer = MemoryUtil.memAlloc((int)filechannel.size() + 1);

            while (filechannel.read(bytebuffer) != -1)
            {
            }
        }
        else
        {
            bytebuffer = MemoryUtil.memAlloc(8192);
            ReadableByteChannel readablebytechannel = Channels.newChannel(inputStreamIn);

            while (readablebytechannel.read(bytebuffer) != -1)
            {
                if (bytebuffer.remaining() == 0)
                {
                    bytebuffer = MemoryUtil.memRealloc(bytebuffer, bytebuffer.capacity() * 2);
                }
            }
        }

        return bytebuffer;
    }
}

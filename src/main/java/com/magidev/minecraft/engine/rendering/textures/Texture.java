package com.magidev.minecraft.engine.rendering.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture
{
    private final int id;
    private final int width;
    private final int height;

    public Texture(InputStream textureStream)
    {
        try (var stack = stackPush())
        {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channelsBuffer = stack.mallocInt(1);

            if (textureStream == null)
                throw new RuntimeException("The texture stream is null!");

            byte[] imageBytes = textureStream.readAllBytes();
            if (imageBytes == null || imageBytes.length == 0)
                throw new RuntimeException("The image data is empty or null.");

            ByteBuffer imageDataBuffer = ByteBuffer.allocateDirect(imageBytes.length).put(imageBytes);
            imageDataBuffer.flip();

            STBImage.stbi_set_flip_vertically_on_load(true);

            ByteBuffer imageData = STBImage.stbi_load_from_memory(imageDataBuffer, widthBuffer, heightBuffer, channelsBuffer, 4);
            if (imageData == null)
                throw new RuntimeException("Error when loading the texture : " + STBImage.stbi_failure_reason());

            width = widthBuffer.get();
            height = heightBuffer.get();

            id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            STBImage.stbi_image_free(imageData);
        } catch (Exception e) {
            throw new RuntimeException("Error when loading the texture", e);
        }
    }

    public void bind(int textureUnit)
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void cleanup()
    {
        GL11.glDeleteTextures(id);
    }
}

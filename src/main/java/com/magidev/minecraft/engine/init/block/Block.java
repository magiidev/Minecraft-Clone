package com.magidev.minecraft.engine.init.block;

import com.magidev.minecraft.Minecraft;
import com.magidev.minecraft.engine.rendering.objects.Model;
import com.magidev.minecraft.engine.rendering.shaders.StaticShader;
import com.magidev.minecraft.engine.rendering.textures.Texture;
import com.magidev.minecraft.engine.utils.Utils;

public class Block
{
    private int id;
    private String name;

    private Model model;
    private BlockType blockType;

    private boolean doesNeedsDifferentTextures;

    private Texture sideTexture;
    private Texture topTexture;
    private Texture bottomTexture;

    public Block(int id, String name)
    {
        this(id, name, BlockType.SOLID);
    }

    public Block(int id, String name, BlockType blockType)
    {
        this.id = id;
        this.name = name;

        switch(blockType)
        {
            case SOLID -> this.model = BlockMesher.createBlockModel();
            case SLAB -> this.model = BlockMesher.createBlockSlabModel();
            //case BILLBOARD -> this.model = BlockMesher.createBlockModel();
            // case TRANSPARENT -> this.model = BlockMesher.createBlockModel();
        }

        this.blockType = blockType;

        searchForDifferentTextures();

        if (doesNeedsDifferentTextures)
        {
            topTexture = new Texture(Utils.getResource("textures/blocks/" + name + "_top.png"));
            bottomTexture = new Texture(Utils.getResource("textures/blocks/" + name + "_bottom.png"));
            sideTexture = new Texture(Utils.getResource("textures/blocks/" + name + ".png"));

            model.setSideTexture(sideTexture);
            model.setTopTexture(topTexture);
            model.setBottomTexture(bottomTexture);
        } else
        {
            sideTexture = new Texture(Utils.getResource("textures/blocks/" + name + ".png"));

            model.setSideTexture(sideTexture);
        }
    }

    private void searchForDifferentTextures()
    {
        doesNeedsDifferentTextures = Utils.fileExists("assets/minecraft/textures/blocks/" + name + "_top.png") && Utils.fileExists("assets/minecraft/textures/blocks/" + name + "_bottom.png");

        System.out.println(name + (doesNeedsDifferentTextures ? " needs different textures." : " doesn't need different textures."));
    }

    public void render()
    {
        StaticShader shader = Minecraft.getStaticShader();

        if (doesNeedsDifferentTextures)
        {
            shader.setUsingDifferentTextures(true);
        } else {
            shader.setUsingDifferentTextures(false);
        }

        model.render(doesNeedsDifferentTextures);
    }

    public void cleanup()
    {
        model.cleanup();
    }

    public int getID()
    {
        return id;
    }

    public Model getModel()
    {
        return model;
    }

    public BlockType getBlockType()
    {
        return blockType;
    }

    public Texture getSideTexture()
    {
        return sideTexture;
    }

    public Texture getTopTexture()
    {
        return topTexture;
    }

    public Texture getBottomTexture()
    {
        return bottomTexture;
    }
}

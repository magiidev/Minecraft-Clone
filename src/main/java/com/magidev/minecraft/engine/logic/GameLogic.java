package com.magidev.minecraft.engine.logic;

public interface GameLogic
{
    void init();
    void render();
    void input(double TICK_RATE);
    void update(double TICK_RATE);
    void cleanup();
}

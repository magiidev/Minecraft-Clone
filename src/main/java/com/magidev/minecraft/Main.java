package com.magidev.minecraft;

import com.magidev.minecraft.config.Config;
import com.magidev.minecraft.engine.logic.GameLogic;
import com.magidev.minecraft.engine.utils.Utils;
import com.magidev.minecraft.engine.window.Window;

import java.io.InputStream;

public class Main
{
    private static Minecraft mc;
    private static Window window;

    public static void main(String[] args)
    {
        /*-----------------------------------------Window---------------------------------------------*/
        window = new Window(Config.NAME, 1280, 720, false);
        window.init();

        /*-----------------------------------------Icons---------------------------------------------*/
        InputStream icon16 = Utils.getResource("icons/icon_16x16.png");
        InputStream icon32 = Utils.getResource("icons/icon_32x32.png");
        /*-------------------------------------------------------------------------------------------*/

        window.setIcon(icon16, icon32);
        /*---------------------------------------------------------------------------------------------*/

        mc = new Minecraft();
        mc.init();

        gameLoop(mc, window);
    }


    private static void gameLoop(GameLogic game, Window window)
    {
        double lastTime = System.nanoTime() / 1_000_000_000.0;
        int frames = 0;
        double fpsTimer = 0.0;

        while (!window.shouldClose())
        {
            Utils.updateViewportIfNeeded(window);

            double currentTime = System.nanoTime() / 1_000_000_000.0;
            double deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            frames++;
            fpsTimer += deltaTime;

            // Affiche les FPS dans le titre de la fenêtre toutes les secondes
            if (fpsTimer >= 1.0)
            {
                int fps = frames;
                frames = 0;
                fpsTimer = 0.0;
                window.setTitle(Config.NAME + " | FPS: " + fps);
            }

            // Mise à jour du jeu avec deltaTime
            game.input(deltaTime);   // Passe deltaTime à la logique d'entrée
            game.update(deltaTime); // Mets à jour la logique du jeu avec deltaTime

            // Rendu graphique
            game.render();

            // Mise à jour de la fenêtre
            window.update();
        }

        game.cleanup();
        window.close();
    }

    public static Window getWindow()
    {
        return window;
    }
}
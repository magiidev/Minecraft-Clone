package com.magidev.minecraft;

import com.magidev.minecraft.config.Config;
import com.magidev.minecraft.engine.logic.GameLogic;
import com.magidev.minecraft.engine.utils.Utils;
import com.magidev.minecraft.engine.window.Window;

import java.io.InputStream;

public class Main
{
    private static Minecraft mc;

    public static void main(String[] args)
    {
        /*-----------------------------------------Window---------------------------------------------*/
        Window window = new Window(Config.NAME, 1280, 720, false);
        window.init();

        /*-----------------------------------------Icons---------------------------------------------*/
        InputStream icon16 = Utils.getResource("/assets/minecraft/icons/icon_16x16.png");
        InputStream icon32 = Utils.getResource("/assets/minecraft/icons/icon_32x32.png");
        /*-------------------------------------------------------------------------------------------*/

        window.setIcon(icon16, icon32);
        /*---------------------------------------------------------------------------------------------*/

        mc = new Minecraft();
        mc.init();

        gameLoop(mc, window);
    }

    private static void gameLoop(GameLogic game, Window window)
    {
        final double TICK_RATE = 1.0 / 20.0; // 20 ticks par seconde
        double lastTime = System.nanoTime() / 1000000000.0;
        double accumulator = 0.0;

        int frames = 0;
        double fpsTimer = 0.0;

        while (!window.shouldClose())
        {
            Utils.updateViewportIfNeeded(window);

            double currentTime = System.nanoTime() / 1000000000.0;
            double frameTime = currentTime - lastTime;
            lastTime = currentTime;

            accumulator += frameTime;

            frames++;
            fpsTimer += frameTime;

            // Affiche les FPS dans le titre de la fenêtre toutes les secondes
            if (fpsTimer >= 1.0)
            {
                int fps = frames;
                frames = 0;
                fpsTimer = 0.0;

                window.setTitle(Config.NAME + " | FPS: " + fps);
            }

            // Traitement des ticks fixes
            while (accumulator >= TICK_RATE)
            {
                game.input(TICK_RATE);   // Passe le temps fixe à la logique d'entrée
                game.update(TICK_RATE); // Mets à jour la logique du jeu avec le temps fixe
                accumulator -= TICK_RATE;
            }

            // Rendu graphique (interpolation possible pour plus de fluidité)
            game.render();

            // Mise à jour de la fenêtre
            window.update();
        }

        game.cleanup();
        window.close();
    }
}
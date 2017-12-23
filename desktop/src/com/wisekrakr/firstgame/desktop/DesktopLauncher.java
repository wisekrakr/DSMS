package com.wisekrakr.firstgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wisekrakr.firstgame.SpaceGameContainer;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 1080;
        config.height = 720;
        config.title = "Don't shoot my space ship!!!";

        new LwjglApplication(new SpaceGameContainer(), config);
    }
}

package com.wisekrakr.firstgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.wisekrakr.firstgame.SpaceGameContainer;
import com.wisekrakr.firstgame.server.ServerRunner;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 1080;
        config.height = 720;
        config.title = "Don't shoot my space ship!!!";

        String host = null;
        int port = -1;

        if (arg.length != 0) {
            if (arg.length != 2) {
                System.out.println("2 parameters expected: <host> <port>");
                System.exit(-1);
            }

            host = arg[0];
            try {
                port = Integer.parseInt(arg[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port: not a number: " + e.getMessage());
                System.exit(-1);
            }
        }

        if (host == null) {
            // TODO: choose a port
            host = "localhost";
            port = 12345;
            ServerRunner server = new ServerRunner(port);
            server.start();

        }

        new LwjglApplication(new SpaceGameContainer(host, port), config);
    }
}

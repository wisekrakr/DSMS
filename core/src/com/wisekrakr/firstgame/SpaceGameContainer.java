package com.wisekrakr.firstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.wisekrakr.firstgame.Screens.StartScreen;
import com.wisekrakr.firstgame.client.ClientConnector;

import java.net.InetSocketAddress;


public class SpaceGameContainer extends Game {
    private String host;
    private int port;

    public SpaceGameContainer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void create() {
        final ClientConnector connector = new ClientConnector(new InetSocketAddress(host, port));
        try {
            connector.start();
        } catch (Exception e) {
            System.out.println("Unable to set up a connection to the server, will exit");

            Gdx.app.exit();

            return;
        }

        setScreen(new StartScreen(this, connector));
    }

}

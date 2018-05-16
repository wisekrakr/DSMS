package com.wisekrakr.firstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.wisekrakr.firstgame.screens.SplashIntroScreen;
import com.wisekrakr.firstgame.screens.StartScreen;
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

        //setScreen(new StartScreen(this, connector));
        setScreen(new SplashIntroScreen());

        final long splash_start_time = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {

                        long splash_elapsed_time = System.currentTimeMillis() - splash_start_time;
                        if (splash_elapsed_time <= 5000) {
                            Timer.schedule(
                                    new Timer.Task() {
                                        @Override
                                        public void run() {
                                            setScreen(new StartScreen(SpaceGameContainer.this, connector));
                                        }
                                    }, (float)(5000 - splash_elapsed_time) / 1000f);
                        } else {
                            setScreen(new StartScreen(SpaceGameContainer.this, connector));
                        }
                    }
                });
            }
        }).start();
    }

}

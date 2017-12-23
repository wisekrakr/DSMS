package com.wisekrakr.firstgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.Screens.PlayerPerspectiveScreen;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Asteroid;
import com.wisekrakr.firstgame.engine.gameobjects.ChaserEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.Random;


public class SpaceGameContainer extends Game {
    private Player player1;
    private SpaceEngine engine;
    private ChaserEnemy enemy1;
    private Player player2;

    @Override
    public void create() {
        engine = new SpaceEngine(-1000, -1000, 2000, 2000);

        initializeGameObjects();

        setScreen(new PlayerPerspectiveScreen(engine, player1, player1, player2));

        Thread timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                engine.elapseTime(0);
                long then = System.nanoTime();
                while (true) {
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        break;
                    }

                    long now = System.nanoTime();
                    float elapsed = ((float) (now - then) / 1000000000L);

                    then = now;

                    engine.elapseTime(elapsed);
                }
            }
        });

        timeThread.setDaemon(true);

        timeThread.start();
    }


    public void initializeGameObjects() {
        Random randomGenerator = new Random();

        Vector2 playerSpawnPoint = new Vector2(0, 0);

        player1 = new Player("Max", playerSpawnPoint, engine);
        player2 = new Player("David", playerSpawnPoint, engine);

        enemy1 = new ChaserEnemy("Chaser1", new Vector2(playerSpawnPoint.x + 30, playerSpawnPoint.y + 30), engine);
        ChaserEnemy enemy2 = new ChaserEnemy("Chaser2", new Vector2(randomGenerator.nextInt(Constants.WORLD_WIDTH),
                randomGenerator.nextInt(Constants.WORLD_HEIGHT)), engine);

        engine.addGameObject(enemy1);
        engine.addGameObject(enemy2);
        engine.addGameObject(player1);
        engine.addGameObject(player2);

        for (int i = 0; i < 100; i++) {
            Asteroid asteroid = new Asteroid("Boeja", new Vector2(
                    randomGenerator.nextFloat() * Constants.WORLD_WIDTH,
                    randomGenerator.nextFloat() * Constants.WORLD_HEIGHT),
                    randomGenerator.nextFloat() * 20, 40 * randomGenerator.nextFloat(), randomGenerator.nextFloat() * 2 * (float) Math.PI, engine, 2f);
            engine.addGameObject(asteroid);
        }
    }

    public Player getPlayer1() {
        return player1;
    }

}

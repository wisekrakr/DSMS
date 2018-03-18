package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.GameState;
import com.wisekrakr.firstgame.client.GameObjectCreationRequest;
import com.wisekrakr.firstgame.client.SpaceshipControlRequest;
import com.wisekrakr.firstgame.engine.MyAssetManager;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.*;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpMissile;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpShield;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Daemon {

    private static SpaceEngine initializeEngine() {
        float minX = -3000;
        float minY = -3000;
        float width = 5000;
        float height = 5000;
        float plusOfXY = 3000;

        SpaceEngine engine = new SpaceEngine(minX, minY, width, height);



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

        Random randomGenerator = new Random();

        PowerUpMissile powerUpMissile = new PowerUpMissile("MissilePowerUp", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                engine);
        engine.addGameObject(powerUpMissile);

        PowerUpShield powerUpShield = new PowerUpShield("ShieldPowerUp", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                engine);
        engine.addGameObject(powerUpShield);

        for (int i = 0; i < 5; i++) {
            EnemyChaser chaser = new EnemyChaser("Chaser", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    50,randomGenerator.nextFloat() * 2000 - 1000,
                    22f, engine);
            engine.addGameObject(chaser);
        }
/*
        for (int i = 0; i < 5; i++) {
            EnemyBlinker enemyBlinker = new EnemyBlinker("Blinker", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    50,randomGenerator.nextFloat() * 2000 - 1000,
                    25f, engine);
            engine.addGameObject(enemyBlinker);
        }

        EnemyMotherShip enemyMotherShip = new EnemyMotherShip("MotherShip", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                200, randomGenerator.nextFloat(),
                90f, engine);
        engine.addGameObject(enemyMotherShip);

        EnemyMutator enemyMutator = new EnemyMutator("SporeShip", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                150, randomGenerator.nextFloat(),
                140f, engine);
        engine.addGameObject(enemyMutator);

        for (int i = 0; i < 10; i++) {
            EnemyDodger enemyDodger = new EnemyDodger("Dodger", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    10,randomGenerator.nextFloat() * 2000 - 1000 ,
                    12f, engine);
            engine.addGameObject(enemyDodger);
        }

        for(int i = 0; i < 10; i++){
            EnemyShotty enemyShotty = new EnemyShotty("Stalker", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    25,randomGenerator.nextFloat() * 2000 - 1000,
                    12f, engine);
            engine.addGameObject(enemyShotty);
        }

        for(int i = 0; i < 3; i++){
            EnemyHomer enemyHomer = new EnemyHomer("MissileEnemy", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    75,randomGenerator.nextFloat() * 2000 - 1000,
                    30f, engine);
            engine.addGameObject(enemyHomer);
        }
*/
        for (int i = 0; i < 20; i++) {
            Asteroid asteroid = new Asteroid("Boeja", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    randomGenerator.nextFloat() * 20,
                    10 * randomGenerator.nextFloat(),
                    randomGenerator.nextFloat() * 2 * (float) Math.PI, engine,
                    6f);
            engine.addGameObject(asteroid);
        }

        timeThread.start();

        return engine;
    }



    public static void main(String[] args) throws Exception {
        SpaceEngine engine = initializeEngine();

        int port = 12345;

        System.out.println("Listing on " + port);

        ServerSocket serverSocket = new ServerSocket(port, 100, InetAddress.getByAddress(new byte[]{0, 0, 0, 0}));

        while (true) {
            System.out.println("Waiting for a  connection...");

            final Socket clientSocket = serverSocket.accept();
            System.out.println("Received a  connection from  " + clientSocket);

            Thread readThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());

                        Object incoming = null;

                        Map<String, Spaceship> myFleet = new HashMap<>();

                        while ((incoming = input.readObject()) != null) {
                            if (incoming instanceof GameObjectCreationRequest) {
                                GameObjectCreationRequest request = (GameObjectCreationRequest) incoming;

                                Player result = new Player(request.getName(), request.getInitialPosition(), engine);

                                engine.addGameObject(result);

                                myFleet.put(request.getName(), result);


                            }
                            else if (incoming instanceof SpaceshipControlRequest) {
                                SpaceshipControlRequest request = (SpaceshipControlRequest) incoming;

                                Spaceship ship = myFleet.get(request.getName());

                                if (ship == null) {
                                    System.out.println("Unknown ship: " + request.getName());
                                }
                                else {
                                    engine.forObject(ship, new SpaceEngine.GameObjectHandler() {
                                        @Override
                                        public void doIt(GameObject target) {
                                            ship.control(request.getThrottleState(), request.getSteeringState(),
                                                    request.getSpecialPowerState(), request.getShootingState());
                                        }
                                    });
                                }
                            }
                            else {
                                System.out.println("Unknown request: " + incoming);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Connection to " + clientSocket + " broken: " + e.getMessage());
                    }

                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        // we'll ignore the error, since it doesn't matter anymore
                    }
                }
            });

            Thread writeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

                        String inMsg = null;

                        while (true) {
                            output.writeObject(engine.makeSnapshot());

                            Thread.sleep(10L);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("Connection to " + clientSocket + " broken: " + e.getMessage());
                    }

                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        // we'll ignore the error, since it doesn't matter anymore
                    }
                }
            });

            readThread.setDaemon(true);
            readThread.start();

            writeThread.setDaemon(true);
            writeThread.start();

        }
    }
}

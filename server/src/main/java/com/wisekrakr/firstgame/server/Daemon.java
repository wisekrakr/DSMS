package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.GameObjectCreationRequest;
import com.wisekrakr.firstgame.client.SpaceshipControlRequest;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Daemon {

    private static SpaceEngine initializeEngine() {
        SpaceEngine engine = new SpaceEngine(-1000, -1000, 2000, 2000);

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

        Vector2 playerSpawnPoint = new Vector2(0, 0);

//        Player player1 = new Player("Max", playerSpawnPoint, engine);
//        Player player2 = new Player("David", playerSpawnPoint, engine);

        ChaserEnemy enemy1 = new ChaserEnemy("Chaser1", new Vector2(playerSpawnPoint.x + 30, playerSpawnPoint.y + 30), engine);
        ChaserEnemy enemy2 = new ChaserEnemy("Chaser2", new Vector2(randomGenerator.nextInt(1000), randomGenerator.nextInt(1000)), engine);

        engine.addGameObject(enemy1);
        engine.addGameObject(enemy2);
//        engine.addGameObject(player1);
//        engine.addGameObject(player2);

        for (int i = 0; i < 100; i++) {
            Asteroid asteroid = new Asteroid("Boeja", new Vector2(
                    randomGenerator.nextFloat() * 2000 - 1000,
                    randomGenerator.nextFloat() * 2000 - 1000),
                    randomGenerator.nextFloat() * 20, 40 * randomGenerator.nextFloat(), randomGenerator.nextFloat() * 2 * (float) Math.PI, engine, 2f);
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
                                            ship.control(request.getThrottleState(), request.getSteeringState());
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

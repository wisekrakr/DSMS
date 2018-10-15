package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.PlayerCreationRequest;
import com.wisekrakr.firstgame.client.PauseUnPauseRequest;
import com.wisekrakr.firstgame.client.SpaceshipControlRequest;
import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gamecharacters.*;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;
import com.wisekrakr.firstgame.engine.scenarios.DamselInDistress;
import com.wisekrakr.firstgame.engine.scenarios.WildlifeManagement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.math.MathUtils.random;

public class ServerRunner {
    private int port;
    private Thread listenThread;
    private GameEngine gameEngine;
    private SpaceEngine engine;

    public ServerRunner(int port) {
        this.port = port;
    }

    public void start() {
        listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listen();
                } catch (Exception e) {
                    System.out.println("Error while listening: " + e.getMessage());

                    e.printStackTrace();
                }
            }
        });

        listenThread.start();
    }

    public void stop() {
        listenThread.interrupt();
    }

    private SpaceEngine initializeEngine() {
        float minX = EngineConstants.MIN_X;
        float minY = EngineConstants.MIN_Y;
        float width = EngineConstants.ENGINE_WIDTH;
        float height = EngineConstants.ENGINE_HEIGHT;
        float plusOfXY = EngineConstants.PLUS_XY;

        engine = new SpaceEngine(minX, minY, width, height);

        gameEngine = new GameEngine(engine);

        Thread timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                gameEngine.elapseTime(0);
                long then = System.nanoTime();
                while (true) {
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        break;
                    }
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start();

                    long now = System.nanoTime();
                    float elapsed = ((float) (now - then) / 1000000000L);

                    then = now;

                    gameEngine.elapseTime(elapsed);

                    stopWatch.printIfLonger("Elapse time of " + elapsed, 100_000_000L);
                }
            }
        });

        timeThread.setDaemon(true);

        //gameEngine.addScenario(new ProtectedConvoy(100, 500, 1, 4, 3));

        //gameEngine.addScenario(new DamselInDistress(300, 100, 0));




/*
        gameEngine.addScenario(new WildlifeManagement(0, 3, engine, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new StandardAggressiveCharacter(position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack,
                        health);
            }
        }));
*/
        gameEngine.addScenario(new WildlifeManagement(3, 1, engine, new CharacterFactory() {
            @Override
            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                return new XCharacter(position,
                        radius,
                        speedDirection,
                        speedMagnitude,
                        radiusOfAttack,
                        health);
            }
        }));

/*
        for (int i = 0; i < 0; i++) {
            gameEngine.addGameCharacter(new XCharacter(GameHelper.randomPosition(),
                    GameHelper.generateRandomNumberBetween(15f, 25f),
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(40f, 70f),
                    GameHelper.generateRandomNumberBetween(200f, 500f),
                    GameHelper.generateRandomNumberBetween(10f, 30f)));
        }



        for (int i = 0; i < 0; i++) {
            gameEngine.addGameCharacter(new StandardAggressiveCharacter(GameHelper.randomPosition(),
                    GameHelper.generateRandomNumberBetween(15f, 25f),
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(40f, 70f),
                    300f,
                    GameHelper.generateRandomNumberBetween(20f, 40f)));
        }
*/
        for (int i = 0; i < 0; i++){
            gameEngine.addGameCharacter(new AsteroidCharacter(GameHelper.randomPosition(),
                    GameHelper.generateRandomNumberBetween(5f, 20f),
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(5f, 60f),
                    10f,
                    10f));

        }

        timeThread.start();


        return engine;
    }


    private void listen() throws Exception {
        SpaceEngine engine = initializeEngine();

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

                        Map<String, Player> myFleet = new HashMap<>();

                        while ((incoming = input.readObject()) != null) {
                            if (incoming instanceof PlayerCreationRequest) {
                                PlayerCreationRequest request = (PlayerCreationRequest) incoming;

                                Player result = new Player(request.getName());
                                gameEngine.addGameCharacter(result);
                                myFleet.put(request.getName(), result);

                            } else if (incoming instanceof SpaceshipControlRequest) {
                                SpaceshipControlRequest request = (SpaceshipControlRequest) incoming;

                                Player player = myFleet.get(request.getName());

                                if (player == null) {
                                    System.out.println("Unknown ship: " + request.getName());
                                } else {
                                    player.control(request);
                                }
                            } else if (incoming instanceof PauseUnPauseRequest) {
                                PauseUnPauseRequest pauseRequest = (PauseUnPauseRequest) incoming;
                                if (pauseRequest.isPause()) {
                                    engine.pause();
                                } else {
                                    engine.resume();
                                }
                            } else {
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

            readThread.setDaemon(true);
            readThread.start();

            writeThread.setDaemon(true);
            writeThread.start();

        }
    }
}

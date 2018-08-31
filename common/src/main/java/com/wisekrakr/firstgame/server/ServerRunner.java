package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.GameObjectCreationRequest;
import com.wisekrakr.firstgame.client.PauseUnPauseRequest;
import com.wisekrakr.firstgame.client.SpaceshipControlRequest;
import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.*;
import com.wisekrakr.firstgame.engine.scenarios.*;

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

        //gameEngine.addScenario(new ProtectedConvoy(100, 500, 3, 7, 3));

        //gameEngine.addScenario(new DamselInDistress(100, 300, 3));

        //gameEngine.addScenario(new TravellerWithMission(300f, 5));

        //gameEngine.addScenario(new BossMommaFight(600f, 10, 3));

        gameEngine.addScenario(new SwarmScenario(200f, 350f, 5, 0));

/*
        gameEngine.addScenario(new WildlifeManagement(2, 1, new GameObjectFactory() {
            @Override
            public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                return new TestNPC(initialPosition, actionDistance);
            }
        }));


        gameEngine.addScenario(new WildlifeManagement(3, 20, new GameObjectFactory() {
            @Override
            public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                return new CrazilySpawningPassiveAggressiveNPC(initialPosition, actionDistance);
            }
        }));

        gameEngine.addScenario(new WildlifeManagement(3, 2, new GameObjectFactory() {
            @Override
            public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                return new FollowingChasingNPC(initialPosition, actionDistance);
            }
        }));


        gameEngine.addScenario(new WildlifeManagement(2, 1, new GameObjectFactory() {
            @Override
            public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                return new AsteroidWatchingMissileShootingNPC(initialPosition, actionDistance);
            }
        }));
*/
        gameEngine.addScenario(new WildlifeManagement(5, 5, new GameObjectFactory() {
            @Override
            public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                return new AsteroidNPC(initialPosition, GameHelper.generateRandomNumberBetween(3f, 20f));
            }
        }));


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

                        Map<String, Spaceship> myFleet = new HashMap<>();

                        while ((incoming = input.readObject()) != null) {
                            if (incoming instanceof GameObjectCreationRequest) {
                                GameObjectCreationRequest request = (GameObjectCreationRequest) incoming;

                                Player result = new Player(request.getName(), request.getInitialPosition());

                                engine.addGameObject(result);

                                myFleet.put(request.getName(), result);
                            } else if (incoming instanceof SpaceshipControlRequest) {
                                SpaceshipControlRequest request = (SpaceshipControlRequest) incoming;

                                Spaceship ship = myFleet.get(request.getName());

                                if (ship == null) {
                                    System.out.println("Unknown ship: " + request.getName());
                                } else {
                                    engine.forObject(ship, new SpaceEngine.GameObjectHandler() {
                                        @Override
                                        public void doIt(GameObject target) {
                                            ship.control(request.getThrottleState(), request.getSteeringState(),
                                                    request.getSpecialPowerState(), request.getShootingState(),
                                                    request.getAimingState(), request.getSwitchWeaponState(),
                                                    request.getHardSteering());
                                        }
                                    });
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

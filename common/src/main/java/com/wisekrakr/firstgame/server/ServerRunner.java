package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.PauseUnPauseRequest;
import com.wisekrakr.firstgame.client.PlayerCreationRequest;
import com.wisekrakr.firstgame.client.SpaceshipControlRequest;
import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gamecharacters.*;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;
import com.wisekrakr.firstgame.engine.scenarios.DamselInDistress;
import com.wisekrakr.firstgame.engine.scenarios.ProtectedConvoy;
import com.wisekrakr.firstgame.engine.scenarios.WildlifeManagement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

    private void initializeEngine() {

        engine = new SpaceEngine();

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

        gameEngine.addScenario(new MyFirstSpaceGame());
        //gameEngine.addScenario(new DamselInDistress(300f, 200f, 1));
        //gameEngine.addScenario(new ProtectedConvoy(500f, 300f, 3, 10, 3));

        timeThread.setDaemon(true);
        timeThread.start();
    }


    private void listen() throws Exception {
        initializeEngine();

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
                                gameEngine.addGameCharacter(result, null); // TODO: what to put in the listener? "You're dead!" ?
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
                        System.out.println("(Read thread in SpaceEngine) Connection to " + clientSocket + " broken: " + e.getMessage());
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
                        System.out.println("(Write thread in SpaceEngine) Connection to " + clientSocket + " broken: " + e.getMessage());
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

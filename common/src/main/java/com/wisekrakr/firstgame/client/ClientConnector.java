package com.wisekrakr.firstgame.client;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientConnector {
    private InetSocketAddress address;
    private Socket clientSocket;
    private final Object monitor = new Object();
    private SpaceSnapshot latestSnapshot;

    private BlockingQueue<Object> queue = new LinkedBlockingDeque<>();

    public ClientConnector(InetSocketAddress address) {
        this.address = address;
    }

    public void start() throws Exception {
        clientSocket = new Socket(address.getAddress(), address.getPort());

        Thread readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());

                    Object incoming = null;

                    while ((incoming = input.readObject()) != null) {
                        if (incoming instanceof SpaceSnapshot) {
                            synchronized (monitor) {
                                latestSnapshot = (SpaceSnapshot) incoming;
                            }
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

                    while (true) {
                        // TODO: this will block indefinitely, even past the end of the connection
                        Object nextCommand = queue.take();
                        output.writeObject(nextCommand);
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

    public void createSpaceship(String name, float startX, float startY) {

        queue.add(new GameObjectCreationRequest(name, "SpaceShip", new Vector2(startX, startY)));
    }

    public void controlSpaceship(String name, Spaceship.ThrottleState throttleState, Spaceship.SteeringState steeringState,
                                 Spaceship.SpecialPowerState specialPowerState, Spaceship.ShootingState shootingState) {
        queue.add(new SpaceshipControlRequest(name, throttleState, steeringState, specialPowerState, shootingState));
    }

    public SpaceSnapshot getLatestSnapshot() {
        return latestSnapshot;
    }

    public void stop() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            // silently ignored
        }
    }

    public SpaceSnapshot latestSnapshot() {
        synchronized (monitor) {
            return latestSnapshot;
        }
    }
}

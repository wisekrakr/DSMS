package com.wisekrakr.firstgame;

import com.badlogic.gdx.Game;
import com.wisekrakr.firstgame.Screens.PlayerPerspectiveScreen;
import com.wisekrakr.firstgame.client.ClientConnector;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.UUID;


public class SpaceGameContainer extends Game {
    @Override
    public void create() {
        ClientConnector connector = new ClientConnector(new InetSocketAddress("localhost", 12345));

        try {
            connector.start();
        } catch (Exception e) {
            System.out.println("Cannot connect: " + e.getMessage());

            return;
        }

        String unique = UUID.randomUUID().toString();

        setScreen(new PlayerPerspectiveScreen(connector, Arrays.asList(unique + "-A", unique + "-B"), unique + "-A"));
    }
}

package com.wisekrakr.firstgame.server;

public class Daemon {
    public static void main(String[] args) throws Exception {
        ServerRunner server;

        server = new ServerRunner(12345);

        server.start();
        try {
            while (true) {
                Thread.sleep(10000L);
            }
        } finally {
            server.stop();
        }

    }
}

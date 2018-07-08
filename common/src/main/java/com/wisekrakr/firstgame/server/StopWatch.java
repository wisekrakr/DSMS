package com.wisekrakr.firstgame.server;

public class StopWatch {
    private long startTime;

    public void start() {
        startTime = System.nanoTime();
    }

    public void printIfLonger(String event, long threshold) {
        long now = System.nanoTime();

        if (now - startTime > threshold) {
            System.out.println(event + " at " + (double) (now - startTime) / 1_000_000_000L);
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs;

public interface Behavior {
    void init(BehaviorContext context);

    void start();

    void stop();

    void elapseTime(float clock, float delta);

}
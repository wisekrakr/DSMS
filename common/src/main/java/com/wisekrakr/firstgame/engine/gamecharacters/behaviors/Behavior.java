package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

public interface Behavior {
    void init(BehaviorContext context);

    void start();

    void stop();

    // TODO: ditch clock?
    void elapseTime(float clock, float delta);

}
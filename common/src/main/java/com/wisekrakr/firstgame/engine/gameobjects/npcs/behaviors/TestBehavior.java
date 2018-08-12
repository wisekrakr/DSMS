package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

public class TestBehavior extends Behavior {

    private float initialDirection;
    private float stopTime;
    private double destructInterval;

    public TestBehavior(float initialDirection, double destructInterval) {
        this.initialDirection = initialDirection;
        this.destructInterval = destructInterval;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        context.setSpeed(GameHelper.generateRandomNumberBetween(200f, 220f));
        context.setDirection(initialDirection);
        context.setOrientation(initialDirection);

        stopTime += delta;

        if (stopTime >= destructInterval){
            int fragments = GameHelper.randomGenerator.nextInt(10)+1;
            for(int i = 0; i < fragments; i++) {
                context.addGameObject(new DebrisObject(context.getPosition(), context.getRadius()));
            }
            context.removeGameObject(context.thisObject());
            stopTime = 0f;

        }
    }
}

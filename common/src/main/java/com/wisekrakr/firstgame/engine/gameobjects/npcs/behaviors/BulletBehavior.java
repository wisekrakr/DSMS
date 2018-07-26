package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;

import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class BulletBehavior extends Behavior {

    private float initialDirection;

    public BulletBehavior(float initialDirection) {
        this.initialDirection = initialDirection;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        context.setSpeed(GameHelper.generateRandomNumberBetween(200f, 220f));
        context.setDirection(initialDirection);
        context.setOrientation(initialDirection);



    }
}

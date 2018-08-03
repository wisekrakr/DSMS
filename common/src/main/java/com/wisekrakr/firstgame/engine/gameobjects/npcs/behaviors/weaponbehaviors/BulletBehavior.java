package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;

import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class BulletBehavior extends Behavior {

    private float initialDirection;
    private float lastShot;
    private double destructInterval;

    public BulletBehavior(float initialDirection, double destructInterval) {
        this.initialDirection = initialDirection;
        this.destructInterval = destructInterval;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        context.setSpeed(GameHelper.generateRandomNumberBetween(200f, 220f));
        context.setDirection(initialDirection);
        context.setOrientation(initialDirection);

        lastShot += delta;

        if (lastShot >= destructInterval){
            context.removeGameObject(context.thisObject());
            lastShot = 0f;
        }
    }
}

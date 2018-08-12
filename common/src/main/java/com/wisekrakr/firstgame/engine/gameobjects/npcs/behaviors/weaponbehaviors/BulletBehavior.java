package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;

import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class BulletBehavior extends Behavior {
    private float lastShot;
    private double destructInterval;
    private float speed;

    public BulletBehavior(double destructInterval, float speed) {
        this.destructInterval = destructInterval;
        this.speed = speed;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        context.setSpeed(speed);

        lastShot += delta;

        if (lastShot >= destructInterval){
            context.removeGameObject(context.thisObject());
            lastShot = 0f;
        }
    }
}

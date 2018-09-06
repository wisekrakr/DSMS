package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class BulletBehavior extends Behavior {
    private float lastShot;

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        context.setSpeed(200f);

        lastShot += delta;

        if (lastShot >= 3f){
            context.removeGameObject(context.thisObject());
            lastShot = 0f;
        }
    }
}

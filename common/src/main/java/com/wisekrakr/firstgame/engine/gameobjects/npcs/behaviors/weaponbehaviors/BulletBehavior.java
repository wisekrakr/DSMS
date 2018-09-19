package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class BulletBehavior extends AbstractBehavior {
    private float lastShot;

    @Override
    public void elapseTime(float clock, float delta) {
        getContext().setSpeed(200f);

        lastShot += delta;

        if (lastShot >= 3f){
            getContext().removeGameObject(getContext().thisObject());
            lastShot = 0f;
        }
    }
}

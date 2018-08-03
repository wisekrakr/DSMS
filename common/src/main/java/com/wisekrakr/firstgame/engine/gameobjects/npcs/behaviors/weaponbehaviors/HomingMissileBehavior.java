package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class HomingMissileBehavior extends Behavior {


    private float initialDirection;
    private double destructInterval;
    private GameObject target;
    private float lastShot;

    public HomingMissileBehavior(float initialDirection, double destructInterval, GameObject target) {

        this.initialDirection = initialDirection;
        this.destructInterval = destructInterval;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null) {
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(100f);
        }

        lastShot += delta;
        if (lastShot >= destructInterval) {
            context.removeGameObject(context.thisObject());
            lastShot = 0f;
        }

    }
}

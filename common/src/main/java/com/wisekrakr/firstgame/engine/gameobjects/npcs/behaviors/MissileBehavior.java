package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class MissileBehavior extends Behavior {

    private float initialDirection;
    private GameObject target;

    public MissileBehavior(float initialDirection, GameObject target) {
        this.initialDirection = initialDirection;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        context.setDirection(initialDirection);
        context.setOrientation(initialDirection);
        context.setSpeed(50f);

        if (target != null) {

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(100f);

        }
    }
}

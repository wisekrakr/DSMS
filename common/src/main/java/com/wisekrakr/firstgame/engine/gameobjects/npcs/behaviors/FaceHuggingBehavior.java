package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class FaceHuggingBehavior extends Behavior {
    private float rotationAngle;
    private GameObject target;
    private float updatedAngle;

    public FaceHuggingBehavior(float rotationAngle, GameObject target) {
        this.rotationAngle = rotationAngle;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null){

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());
            updatedAngle += rotationAngle *  delta;


            context.setOrientation(angle );
            context.setDirection(angle + updatedAngle);
        }
    }
}

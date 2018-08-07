package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class BlinkingBehavior extends Behavior {

    private GameObject target;
    private float blinkInterval;
    private float blinkingTime;

    public BlinkingBehavior(GameObject target, float blinkInterval) {
        this.target = target;
        this.blinkInterval = blinkInterval;
    }



    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null && !(target.getClass() == context.thisObject().getClass())){
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setOrientation(angle);

            if (context.getSpeed() == 0){
                context.setSpeed(30f);
            }else {
                context.setSpeed(context.getSpeed());
            }


            blinkingTime += delta;

        }
    }
}

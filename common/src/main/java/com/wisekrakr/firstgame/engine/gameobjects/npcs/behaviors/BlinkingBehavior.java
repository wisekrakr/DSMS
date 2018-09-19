package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class BlinkingBehavior extends AbstractBehavior {

    //TODO: make this a jump through portal behavior or something
    private GameObject target;
    private float blinkInterval;
    private float blinkingTime;

    public BlinkingBehavior(GameObject target, float blinkInterval) {
        this.target = target;
        this.blinkInterval = blinkInterval;
    }



    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null && !(target.getClass() == getContext().thisObject().getClass())){
            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setOrientation(angle);

            if (getContext().getSpeed() == 0){
                getContext().setSpeed(30f);
            }else {
                getContext().setSpeed(getContext().getSpeed());
            }


            blinkingTime += delta;

        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class SplashBehavior extends Behavior {
    private float timeToSplash;

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (timeToSplash == 0){
            timeToSplash = clock;
        }

        context.setDirection(GameHelper.randomGenerator.nextFloat() * 2f * (float) Math.PI);
        context.setOrientation(GameHelper.randomGenerator.nextFloat() * 10f);
        context.setSpeed(GameHelper.randomGenerator.nextFloat() * 40f);

        if (clock - timeToSplash >= 2){
            context.removeGameObject(context.thisObject());
            timeToSplash = clock;
        }
    }
}

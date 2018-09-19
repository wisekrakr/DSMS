package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;

public class SplashBehavior extends AbstractBehavior {
    private float timeToSplash;
    private float initialDirection;
    private double destructInterval;

    public SplashBehavior(float initialDirection, double destructInterval) {
        this.initialDirection = initialDirection;
        this.destructInterval = destructInterval;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (timeToSplash == 0){
            timeToSplash = clock;
        }

        getContext().setSpeed(GameHelper.generateRandomNumberBetween(150f, 180f));
        getContext().setDirection(initialDirection);
        getContext().setOrientation(initialDirection);

        if (clock - timeToSplash >= destructInterval){
            int fragments = GameHelper.randomGenerator.nextInt(10)+1;
            for(int i = 0; i < fragments; i++) {
                getContext().addGameObject(new BulletObject(getContext().getPosition(), GameHelper.randomDirection(), getContext().thisObject()));
            }
            getContext().removeGameObject(getContext().thisObject());
            timeToSplash = clock;
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;

public class SplashBehavior extends Behavior {
    private float timeToSplash;
    private float initialDirection;
    private double destructInterval;

    public SplashBehavior(float initialDirection, double destructInterval) {
        this.initialDirection = initialDirection;
        this.destructInterval = destructInterval;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (timeToSplash == 0){
            timeToSplash = clock;
        }

        context.setSpeed(GameHelper.generateRandomNumberBetween(150f, 180f));
        context.setDirection(initialDirection);
        context.setOrientation(initialDirection);

        if (clock - timeToSplash >= destructInterval){
            int fragments = GameHelper.randomGenerator.nextInt(10)+1;
            for(int i = 0; i < fragments; i++) {
                context.addGameObject(new BulletObject(context.getPosition(), GameHelper.randomDirection(), 3f, context.thisObject(), 200));
            }
            context.removeGameObject(context.thisObject());
            timeToSplash = clock;
        }
    }
}

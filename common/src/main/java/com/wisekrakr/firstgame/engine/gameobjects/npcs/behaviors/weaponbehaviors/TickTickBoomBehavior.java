package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.SpaceMineObject;

public class TickTickBoomBehavior extends Behavior {
    private float initialDirection;
    private float stopTime;
    private double destructInterval;

    public TickTickBoomBehavior(float initialDirection, double destructInterval) {
        this.initialDirection = initialDirection;
        this.destructInterval = destructInterval;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        context.setSpeed(GameHelper.generateRandomNumberBetween(200f, 220f));
        context.setDirection(initialDirection);
        context.setOrientation(initialDirection);

        stopTime += delta;

        if (stopTime >= destructInterval){
            int fragments = GameHelper.randomGenerator.nextInt(10)+1;
            for(int i = 0; i < fragments; i++) {
                GameObject o = new SpaceMineObject(context.getPosition(),
                        2f,context.thisObject());

                context.addGameObject(o);
            }
            context.removeGameObject(context.thisObject());
            stopTime = 0f;

        }
    }
}

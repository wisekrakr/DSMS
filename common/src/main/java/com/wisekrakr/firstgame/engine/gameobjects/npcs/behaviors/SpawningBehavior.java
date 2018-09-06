package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;

public class SpawningBehavior extends Behavior {
    private Float lastCreation;
    private GameObjectFactory factory;
    private float spawnInterval;

    public SpawningBehavior(GameObjectFactory factory, float spawnInterval) {
        this.factory = factory;
        this.spawnInterval = spawnInterval;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        if (lastCreation == null) {
            lastCreation = clock;
        }

        if ((clock - lastCreation) > spawnInterval) {
            context.addGameObject(factory.create(context.getPosition(), GameHelper.randomDirection(), context.getActionDistance()));
            lastCreation = clock;
        }

        //context.addGameObject(factory.create(context.getPosition(), GameHelper.randomDirection()));
    }
}
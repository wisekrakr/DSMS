package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;

public class SpawningBehavior extends AbstractBehavior {
    private Float lastCreation;
    private GameObjectFactory factory;
    private float spawnInterval;

    public SpawningBehavior(GameObjectFactory factory, float spawnInterval) {
        this.factory = factory;
        this.spawnInterval = spawnInterval;
    }

    @Override
    public void elapseTime(float clock, float delta) {
        if (lastCreation == null) {
            lastCreation = clock;
        }

        if ((clock - lastCreation) > spawnInterval) {
            getContext().addGameObject(factory.create(getContext().getPosition(), GameHelper.randomDirection(), getContext().getActionDistance()));
            lastCreation = clock;
        }

        //getContext().addGameObject(factory.create(getContext().getPosition(), GameHelper.randomDirection()));
    }
}

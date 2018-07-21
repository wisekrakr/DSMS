package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;

public class SpawningBehavior extends Behavior {
    private NonPlayerCharacter character;
    private Float lastCreation;
    private GameObjectFactory factory;
    private float spawnInterval;

    public SpawningBehavior(GameObjectFactory factory, float spawnInterval) {
        this.factory = factory;
        this.spawnInterval = spawnInterval;
    }

    public SpawningBehavior(NonPlayerCharacter character, float spawnInterval) {
        this.character = character;
        this.spawnInterval = spawnInterval;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        if (lastCreation == null) {
            lastCreation = clock;
        }

        if ((clock - lastCreation) > spawnInterval) {
            context.addGameObject(factory.create(context.getPosition(), GameHelper.randomDirection()));
            lastCreation = clock;
        }
    }
}

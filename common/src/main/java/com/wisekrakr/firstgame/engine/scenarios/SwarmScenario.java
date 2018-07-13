package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class SwarmScenario extends Scenario{
    private float minCreationInterval;
    private float lastCreation = 0f;
    private int numberOfEnemies;
    private Function<Vector2, Enemy>factory;

    public SwarmScenario(float minCreationInterval, int numberOfEnemies, Function<Vector2, Enemy> factory) {

        this.minCreationInterval = minCreationInterval;
        this.numberOfEnemies = numberOfEnemies;
        this.factory = factory;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (lastCreation + minCreationInterval <= spaceEngine.getTime()) {
            lastCreation = spaceEngine.getTime();
            minCreationInterval = 200f;

            for (int i = 0; i < numberOfEnemies; i++){
                Enemy newObject = factory.apply(GameHelper.randomPosition());
/*
                spaceEngine.addGameObject(newObject);
                newObject.setMovingState(Enemy.MovingState.DEFAULT_FORWARDS);
                newObject.setAggroDistance((float) Double.POSITIVE_INFINITY);
                */
            }
        }
    }
}

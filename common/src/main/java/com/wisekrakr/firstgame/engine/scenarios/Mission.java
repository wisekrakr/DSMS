package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.missions.QuestGen;

import java.util.Random;
import java.util.function.Function;

public class Mission extends Scenario {

    private float lastCreation = 0f;
    private float minCreationInterval;
    private Function<Vector2, QuestGen> factory;
    private Random randomGenerator = new Random();

    public Mission(float minCreationInterval, Function<Vector2, QuestGen> factory) {
        this.minCreationInterval = minCreationInterval;
        this.factory = factory;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (lastCreation + minCreationInterval <= spaceEngine.getTime()) {
            lastCreation = spaceEngine.getTime();

            QuestGen quest = factory.apply(GameHelper.randomPosition());

            spaceEngine.addGameObject(quest);
        }
    }


}

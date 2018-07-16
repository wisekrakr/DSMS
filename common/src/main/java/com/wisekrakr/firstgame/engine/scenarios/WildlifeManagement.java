package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class WildlifeManagement extends Scenario {
    private float minCreationInterval;
    private float lastCreation = 0f;
    private GameObjectFactory factory;
    private int targetCount;
    private List<GameObject> myObjects = new ArrayList<>();

    public WildlifeManagement(int targetCount, float minCreationInterval, GameObjectFactory factory) {
        this.targetCount = targetCount;
        this.minCreationInterval = minCreationInterval;
        this.factory = factory;
    }

    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (targetCount > myObjects.size() && lastCreation + minCreationInterval <= spaceEngine.getTime()) {
            lastCreation = spaceEngine.getTime();

            GameObject newObject = factory.create(GameHelper.randomPosition(), GameHelper.randomDirection());


            spaceEngine.addGameObject(newObject, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    myObjects.add(newObject);
                }

                @Override
                public void removed() {
                    myObjects.remove(newObject);
                }
            });
        }
    }
}








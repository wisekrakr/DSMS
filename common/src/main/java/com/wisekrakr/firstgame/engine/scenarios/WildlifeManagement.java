package com.wisekrakr.firstgame.engine.scenarios;

import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;

import java.util.ArrayList;
import java.util.List;

public class WildlifeManagement extends Scenario {
    private float minCreationInterval;
    private CharacterFactory characterFactory;
    private SpaceEngine spaceEngine;
    private float lastCreation = 0f;
    private int targetCount;
    private List<GameCharacter> gameCharacters = new ArrayList<>();

    public WildlifeManagement(int targetCount, float minCreationInterval, SpaceEngine spaceEngine, CharacterFactory characterFactory) {
        this.targetCount = targetCount;
        this.minCreationInterval = minCreationInterval;
        this.characterFactory = characterFactory;
        this.spaceEngine = spaceEngine;
    }

    public void periodicUpdate(SpaceEngine spaceEngine) {

    }

    @Override
    public void characterUpdate(GameEngine gameEngine) {
        if (targetCount > gameCharacters.size() && lastCreation + minCreationInterval <= spaceEngine.getTime()) {
            lastCreation = spaceEngine.getTime();
            //TODO: temporary. character need to be set with a script maybe....to give certain character specific characteristics i.e. position and direction, health and damage etc..

            GameCharacter character = characterFactory.createCharacter(GameHelper.randomPosition(),
                    GameHelper.generateRandomNumberBetween(30f, 70f),
                    GameHelper.randomDirection(),
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(10f, 40f),
                    GameHelper.generateRandomNumberBetween(200f, 600f),
                    50f,
                    10f);

            gameEngine.addGameCharacter(character);
            gameCharacters.add(character);


            //TODO: way to remove characters
        }
    }
}








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
    private float lastCreation = 0f;
    private int targetCount;
    private List<GameCharacter> gameCharacters = new ArrayList<>();

    public WildlifeManagement(int targetCount, float minCreationInterval, CharacterFactory characterFactory) {
        this.targetCount = targetCount;
        this.minCreationInterval = minCreationInterval;
        this.characterFactory = characterFactory;
    }

    public void periodicUpdate() {
        if (targetCount > gameCharacters.size() && lastCreation + minCreationInterval <= getContext().space().getTime()) {
            lastCreation = getContext().space().getTime();
            //TODO: temporary. character need to be set with a script maybe....to give certain character specific characteristics i.e. position and direction, health and damage etc..

            GameCharacter character = characterFactory.createCharacter(GameHelper.randomPosition(),
                    GameHelper.generateRandomNumberBetween(100f, 200f),
                    GameHelper.randomDirection(),
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(20f, 50f),
                    GameHelper.generateRandomNumberBetween(400f, 800f),
                    50f,
                    10f);

            getContext().engine().addGameCharacter(character);
            gameCharacters.add(character);


            //TODO: way to remove characters
        }
    }
}








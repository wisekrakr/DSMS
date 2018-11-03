package com.wisekrakr.firstgame.engine.scenarios;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterListener;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectRunner;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Periodically creates random objects
 */
public class WildlifeManagement extends Scenario {
    /*
       Changes for enlarging space:
         * discard when out of a zone
         * creation algorithm:
            * always in the "creation space"
            * based on the existing concentration
     */
    private float minCreationInterval;
    private CharacterFactory characterFactory;
    private float lastCreation = 0f;
    private float targetDensity;
    private List<GameCharacter> gameCharacters = new ArrayList<>();

    public WildlifeManagement(float targetDensity, float minCreationInterval, CharacterFactory characterFactory) {
        this.targetDensity = targetDensity;
        this.minCreationInterval = minCreationInterval;
        this.characterFactory = characterFactory;
    }

    public void periodicUpdate() {
        float area = getContext().space().getVitalAreaSize();
        float density = gameCharacters.size() / area;

        if (density < targetDensity && lastCreation + minCreationInterval <= getContext().space().getTime()) {
            lastCreation = getContext().space().getTime();
            //TODO: temporary. character need to be set with a script maybe....to give certain character specific characteristics i.e. position and direction, health and damage etc..

            GameCharacter character =
                    characterFactory.createCharacter(
                            getContext().space().chooseCreationPoint(),
                            GameHelper.generateRandomNumberBetween(100f, 200f),
                            GameHelper.randomDirection(),
                            GameHelper.randomDirection(),
                            GameHelper.generateRandomNumberBetween(20f, 50f),
                            GameHelper.generateRandomNumberBetween(400f, 800f)
                    );

            getContext().engine().addGameCharacter(character, new GameCharacterListener() {
                @Override
                public void removed(GameCharacter target) {
                    gameCharacters.remove(target);
                }
            });
            gameCharacters.add(character);

        }
    }


}








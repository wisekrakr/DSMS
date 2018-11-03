package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractNonPlayerGameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.AsteroidCharacter;

import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

public class ScenarioHelper {


    public static final CharacterFactory ASTEROID_FACTORY =
            new CharacterFactory() {
                @Override
                public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                    return new AsteroidCharacter(position, radius, speedDirection, speedMagnitude);
                }
            };

}

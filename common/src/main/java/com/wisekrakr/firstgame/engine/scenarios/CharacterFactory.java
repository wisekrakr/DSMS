package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractGameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractNPCTools;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractNonPlayerGameCharacter;

import java.util.Set;

public interface CharacterFactory<CharacterT extends AbstractGameCharacter> {
    CharacterT createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack);


}

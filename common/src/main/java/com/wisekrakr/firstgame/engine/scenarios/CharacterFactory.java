package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractNonPlayerGameCharacter;

public interface CharacterFactory<CharacterT extends AbstractNonPlayerGameCharacter> {
    CharacterT createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage);

}

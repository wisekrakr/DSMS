package com.wisekrakr.firstgame.engine.gamecharacters;

import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

import java.util.Set;

public interface CharacterTools {
    enum Weaponry{
        NONE, BULLETS, HOMING_MISSILES, MINES, SPLASH_BULLETS
    }

    void addTargetName(String tag);

    void addAvoidName(String tag);

    Set<String> targetList(GameCharacterContext context);

    Set<String> avoidList(GameCharacterContext context);

    void weaponry(GameCharacterContext context, Weaponry weaponry, Float fireRate, Float radiusOfAttack);

    void healthDamage(GameCharacterContext context, PhysicalObject object, float damage);

    void healthIndicator(float initialHealth);

    float getHealth();

}

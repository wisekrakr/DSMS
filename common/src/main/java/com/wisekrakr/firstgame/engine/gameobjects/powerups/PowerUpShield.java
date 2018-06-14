package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.Set;

public class PowerUpShield extends PowerUp {


    public PowerUpShield(String name, Vector2 initialPosition, SpaceEngine space) {
        super(GameObjectType.POWERUP_SHIELD, name, initialPosition, space);
        setCollisionRadius(7.5f);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }
}

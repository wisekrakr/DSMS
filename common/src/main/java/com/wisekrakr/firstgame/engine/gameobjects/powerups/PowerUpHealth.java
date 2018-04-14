package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.Set;

public class PowerUpHealth extends PowerUp {

    public PowerUpHealth(String name, Vector2 initialPosition, SpaceEngine space) {
        super(name, initialPosition, space);
        setCollisionRadius(30);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }
}

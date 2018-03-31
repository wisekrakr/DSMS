package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.Set;

public class PowerUpShield extends GameObject {

    private float time;
    private static final float SPAWN_TIME = 60;

    public PowerUpShield(String name, Vector2 initialPosition, SpaceEngine space) {
        super(name, initialPosition, space);
        setCollisionRadius(30);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        time += delta;

        if(time >= SPAWN_TIME) {
            toAdd.add(new PowerUpShield("powerupshield", new Vector2(setRandomDirection(),
                    setRandomDirection()), getSpace()));
            time=0;
        }
    }
}

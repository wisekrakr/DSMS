package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;

import java.util.Set;

public class HomingWeaponsEnemy extends GameObject {

    private float direction;
    private float radius;

    public HomingWeaponsEnemy(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius) {
        super(name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Asteroid){
            toDelete.add(this);
            toDelete.add(subject);
        }
    }
}

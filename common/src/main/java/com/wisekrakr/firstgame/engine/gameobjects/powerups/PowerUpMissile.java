package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.Set;

public class PowerUpMissile extends GameObject{

    public PowerUpMissile(String name, Vector2 initialPosition, SpaceEngine space) {
        super(name, initialPosition, space);
        setCollisionRadius(30);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            toDelete.add(this);
            ((Player) subject).setMissileAmmoCount(((Player) subject).getMissileAmmoCount() + 20);
        }

    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }
}

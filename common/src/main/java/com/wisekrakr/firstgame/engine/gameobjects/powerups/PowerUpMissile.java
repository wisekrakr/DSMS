package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.PowerUp;

import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

public class PowerUpMissile extends PowerUp{


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
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {


    }
}

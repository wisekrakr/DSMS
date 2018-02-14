package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

public class PowerUpMissile extends GameObject{

    private float time;
    private static final float SPAWN_TIME = 44;

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
        time += delta;

        if(time >= SPAWN_TIME){
            toAdd.add(new PowerUpMissile("powerupmissile", new Vector2(setRandomDirection(),
                    setRandomDirection()),getSpace()));

            time=0;
        }

    }
}

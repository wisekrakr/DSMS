package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Weapons;

import java.util.Set;

public class Shield extends Weapons {

    private float radius;
    private float direction;
    private float time;
    private static final float SHIELD_TIME = 20;

    public Shield(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius) {
        super(name, initialPosition, space, direction, radius);
        this.radius = radius;
        this.direction = direction;

        setCollisionRadius(radius);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Enemy){
            subject.setHealth(subject.getHealth() - 30);
            ((Enemy) subject).setDirection(((Enemy) subject).getDirection() + (float)Math.PI);
            setHealth(getHealth() - 10);
        }
    }

    public float getTime() {
        return time;
    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        time += delta;

        if(this.getTime() >= SHIELD_TIME){
            toDelete.add(this);
            time =0;
        }

    }
}

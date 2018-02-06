package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Weapons;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;

import java.util.Set;

public class Bullet extends Weapons {

    private float direction;
    private float radius;
    private float speed;

    private static final float DEFAULT_BULLET_SPEED = 800;
    private float time;


    public Bullet(String name, Vector2 initialPosition, SpaceEngine space, float direction,float speed, float radius) {
        super(name, initialPosition, space, direction, radius);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;

        setCollisionRadius(4);
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            toDelete.add(this);
        }else if(subject instanceof Asteroid){
            toDelete.add(this);
            toDelete.add(subject);
        }else {
            toDelete.add(this);
        }

    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_BULLET_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_BULLET_SPEED * delta)
        );
        setOrientation(direction);

        float destructTime = 1.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }
    }



    public float getDirection() {
        return direction;
    }


    public float getRadius() {
        return radius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
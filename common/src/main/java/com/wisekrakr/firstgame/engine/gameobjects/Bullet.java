package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.Set;

public class Bullet extends GameObject{

    private float direction;
    private float radius;
    private float speed;

    private static final float DEFAULT_BULLET_SPEED = 800;


    public Bullet(String name, Vector2 initialPosition, SpaceEngine space, float direction,float speed, float radius) {
        super(name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;

        setCollisionRadius(2);
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        toDelete.add(subject);
        toDelete.add(this);

    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_BULLET_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_BULLET_SPEED * delta)
        );
        setOrientation(direction);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        toDelete.add(this);
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

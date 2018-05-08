package com.wisekrakr.firstgame.engine.gameobjects.spaceobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by David on 11/15/2017.
 */
public class Asteroid extends GameObject {
    private float rotationSpeed;
    private float speed;
    private float direction;
    private float radius;

    public Asteroid(String name, Vector2 position, float rotationSpeed, float speed, float direction, SpaceEngine space, float radius) {
        super(name, position, space);

        this.rotationSpeed = rotationSpeed;
        this.speed = speed;
        this.direction = direction;
        this.radius = radius;

        setCollisionRadius(radius);
        setSpeed(speed);
        setRotationSpeed(rotationSpeed);

    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        setOrientation(getOrientation() + getRotationSpeed() * delta);
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * getSpeed() * delta,
                getPosition().y + (float) Math.sin(direction) * getSpeed() * delta)
        );
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Enemy){
            subject.setHealth(subject.getHealth() - 10);
            toDelete.add(this);
            Random random = new Random();
            int debrisParts = random.nextInt(4)+1;
            for(int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", this.getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 30, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getRadius()));

            }
        }
        if (subject instanceof Asteroid) {
            this.setDirection(direction + (float) Math.PI);
        }
        if(subject instanceof Player){
            toDelete.add(this);
            Random random = new Random();
            int debrisParts = random.nextInt(4)+1;
            for(int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", this.getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 30, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getRadius()));

            }
        }
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        toDelete.add(this);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }
}

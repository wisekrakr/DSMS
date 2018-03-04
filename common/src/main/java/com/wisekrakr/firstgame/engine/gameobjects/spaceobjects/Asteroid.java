package com.wisekrakr.firstgame.engine.gameobjects.spaceobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Shield;

import java.util.HashMap;
import java.util.Map;
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
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        setOrientation(getOrientation() + rotationSpeed * delta);
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * speed * delta,
                getPosition().y + (float) Math.sin(direction) * speed * delta)
        );
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Enemy){
            subject.setHealth(subject.getHealth() - 10);
            toDelete.add(this);
        }
        if (subject instanceof Asteroid) {
            radius = radius + ((Asteroid) subject).getRadius();
            setCollisionRadius(radius);

        }
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
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

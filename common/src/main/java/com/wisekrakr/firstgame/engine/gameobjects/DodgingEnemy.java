package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DodgingEnemy extends Enemy {

    private float DEFAULT_ENEMY_SPEED = 20;
    private static final float AGRO_DISTANCE = 80;

    private float direction;
    private float radius;

    public DodgingEnemy(String name, Vector2 position, float direction, float radius, SpaceEngine space) {
        super(name, position, direction, radius, space);
        this.direction = direction;
        this.radius = radius;

        setCollisionRadius(radius);
    }

    @Override
    public void signalOutOfBounds() {
        this.setDirection(-direction);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Asteroid) {
            toDelete.add(this);
        }
        if(subject instanceof Bullet){
            toDelete.add(this);
        }

    }


    @Override
    public void attack(GameObject target) {


        if (target instanceof Player) {

            if (distanceBetween(this, target) <= AGRO_DISTANCE ) {

                float angle = angleBetween(this, target);

                setPosition(new Vector2(getPosition().x -=  Math.cos(angle) *2 , getPosition().y -=  Math.sin(angle) *2 ));

                setOrientation(angle);

                setDirection(angle);

            }
        }
    }


    @Override
    public void elapseTime(float delta) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_ENEMY_SPEED * delta)
        );
        setOrientation(direction);

    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getRadius() {
        return radius;
    }
    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }
}


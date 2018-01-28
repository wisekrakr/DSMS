package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.Map;
import java.util.Set;

public class StalkerEnemy extends Enemy {

    private float DEFAULT_ENEMY_SPEED = 20;
    private static final float AGRO_DISTANCE = 850;
    private static final int CHANGE_DIRECTION_TIME = 3000;
    private float direction;
    private float radius;

    public StalkerEnemy(String name, Vector2 position, float direction, float radius, SpaceEngine space) {
        super(name, position, direction, radius, space);
        setCollisionRadius(5);
    }

    @Override
    public void signalOutOfBounds() {
        super.signalOutOfBounds();
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

       toDelete.remove(subject);
    }

    @Override
    public void attack(GameObject target) {

        if (target instanceof Player) {

            if (distanceBetween(this, target) <= AGRO_DISTANCE ) {

                float angle = angleBetween(this, target);

// to make the chaser chase the player with less vigilance, divide cos and sin by 2
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle) , getPosition().y +=  Math.sin(angle) ));

                setOrientation(angle);

                setDirection(angle);


            }
        }
    }



    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {


        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_ENEMY_SPEED * delta)
        );
        setOrientation(direction);
    }

    @Override
    public float getDirection() {
        return super.getDirection();
    }

    @Override
    public void setDirection(float direction) {
        super.setDirection(direction);
    }

    @Override
    public float getRadius() {
        return super.getRadius();
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        return super.getExtraSnapshotProperties();
    }
}

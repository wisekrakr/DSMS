package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ChaserEnemy extends Enemy{

    private float DEFAULT_ENEMY_SPEED = 80;
    private static final float AGRO_DISTANCE = 250;
    private static final int CHANGE_DIRECTION_TIME = 3000;
    private float direction;
    private float radius;
    private float distance;

    public ChaserEnemy(String name, Vector2 position, float direction, float radius, SpaceEngine space) {
        super(name, position, direction, radius, space);
        this.direction = direction;
        this.radius = radius;

        setCollisionRadius(radius);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        this.setDirection(-direction);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        //        if (!(subject instanceof Enemy)) {
        toDelete.add(subject);

        if (subject instanceof Asteroid) {
            toDelete.add(this);
        }

        if(subject instanceof Bullet){
            toDelete.add(this);
        }
//        }
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
 //Todo: see if the timer works....to change direction of the chaser

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

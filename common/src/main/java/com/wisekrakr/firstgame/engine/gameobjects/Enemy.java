package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Enemy extends GameObject {

    private float direction;
    private float radius;
    private int health;

    private static final float CLOSEST_TARGET = 100;


    public Enemy(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(name, position, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;


        setCollisionRadius(radius);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(direction == direction){
            this.setDirection(-direction);
        }else{
            this.setDirection(direction);
        }

    }

    public enum AttackState {
        PACIFIST, CHASE, SHOOT, SELF_DESTRUCT;
    }


    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(target instanceof Player){
            if(distanceBetween(this, target)< CLOSEST_TARGET){
                attackTarget(target, toDelete, toAdd);
            }
        }
    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (health <= 0) {
            toDelete.add(this);
        }
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

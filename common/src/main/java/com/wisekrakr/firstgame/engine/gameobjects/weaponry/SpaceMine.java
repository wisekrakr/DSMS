package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpaceMine extends GameObject {

    private float speed;
    private float direction;
    private float radius;
    private float time;
    private float destructTime;
    private int damage;


    public SpaceMine(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;
        this.damage = damage;

        setCollisionRadius(radius);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        destructTime = getDestructTime();
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

    }

    public float getDestructTime() {
        return destructTime;
    }

    public void setDestructTime(float destructTime) {
        this.destructTime = destructTime;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }
}

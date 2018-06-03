package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

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
    private boolean isDestruct;


    public SpaceMine(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(GameObjectType.SPACE_MINE, name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;
        this.damage = damage;

        setCollisionRadius(radius);
        isDestruct = false;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        destructTime = getDestructTime();
        time += delta;
        if(time >= destructTime){
            if(!(isDestruct)) {
                toDelete.add(this);
                setDestruct(true);
            }else {
                setDestruct(false);
            }
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

    public boolean isDestruct() {
        return isDestruct;
    }

    public void setDestruct(boolean destruct) {
        isDestruct = destruct;
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

    @Override
    public Map<String, Object> getRandomProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("isDestruct", isDestruct);

        return result;
    }
}

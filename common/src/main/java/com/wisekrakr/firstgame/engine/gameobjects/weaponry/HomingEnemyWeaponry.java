package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.HashMap;
import java.util.Map;

public abstract class HomingEnemyWeaponry extends GameObject{

    private float radius;
    private int damage;
    private float direction;
    private float speed;
    private GameObjectType type;


    protected HomingEnemyWeaponry(GameObjectType type, String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(type, name, initialPosition, space);
        this.type = type;
        this.radius = radius;
        this.damage = damage;
        this.direction = direction;
        this.speed = speed;

        setCollisionRadius(radius);
        setDamage(damage);
        setSpeed(speed);
    }



    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public GameObjectType getType() {
        return type;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }
}

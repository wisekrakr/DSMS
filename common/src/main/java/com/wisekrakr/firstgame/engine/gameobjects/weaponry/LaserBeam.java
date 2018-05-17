package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LaserBeam extends GameObject {

    private float direction;
    private float radius;
    private float speed;
    private int damage;

    private static final float DEFAULT_BULLET_SPEED = 1200;
    private float time;

    public LaserBeam(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius, int damage) {
        super(GameObjectType.LASER_BEAM, name, initialPosition, space);
        this.radius = radius;
        this.damage = damage;
        this.direction = direction;

        setCollisionRadius(radius);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_BULLET_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_BULLET_SPEED * delta)
        );
        setOrientation(direction);

        float destructTime = 1.5f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }
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

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }
}

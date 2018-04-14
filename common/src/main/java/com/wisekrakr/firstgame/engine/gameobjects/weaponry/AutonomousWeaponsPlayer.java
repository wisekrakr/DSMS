package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutonomousWeaponsPlayer extends GameObject {

    private float direction;
    private float radius;
    private int damage;
    private float time;

    private static final float DEFAULT_MISSILE_SPEED = 550;

    public AutonomousWeaponsPlayer(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius, int damage) {
        super(name, initialPosition, space);
        this.radius = radius;
        this.damage = damage;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Asteroid){
            toDelete.add(this);
            toDelete.add(subject);
        }
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

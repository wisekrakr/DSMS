package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Minion extends GameObject {

    private Vector2 position;
    private float direction;
    private float radius;
    private int health;

    private float time;
    private int damage;
    private float destructTime;

    public Minion(String name, Vector2 position, int health, float direction, float radius,  SpaceEngine space) {
        super(name, position, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.position = getPosition();

        setCollisionRadius(radius);
        setHealth(health);

        damage = 10;

    }

    public enum MinionState {
        PACIFIST, SHOOT, RETURN
    }

    public void minionBounds(GameObject object, Set<GameObject> toDelete, Set<GameObject> toAdd){

    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (health <= 0){
            toDelete.add(this);
        }

        destructTime = 30f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

    }


    public int getDamage() {
        return damage;
    }


    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }

    @Override
    public Map<String, Object> getHealthProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("health", health);

        return result;
    }

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }

}

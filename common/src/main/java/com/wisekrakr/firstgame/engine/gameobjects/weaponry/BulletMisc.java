package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BulletMisc extends Bullet {

    private int damage;

    public BulletMisc(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius) {
        super(name, initialPosition, space, direction, speed, radius);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Enemy){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());

        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }
}


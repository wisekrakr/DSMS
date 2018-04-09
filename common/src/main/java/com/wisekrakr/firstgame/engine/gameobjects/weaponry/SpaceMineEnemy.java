package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SpaceMineEnemy extends SpaceMine {

    private int damage;
    private float radius;

    public SpaceMineEnemy(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius) {
        super(name, initialPosition, space, direction, speed, radius);
        setDestructTime(10f);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Player){
            toDelete.add(this);
            damage = randomDamageCountMine();
            subject.setHealth(subject.getHealth() - damage);

            Random random = new Random();
            int debrisParts = random.nextInt(10)+1;
            for(int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 60, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getRadius()));

            }
        }
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


}

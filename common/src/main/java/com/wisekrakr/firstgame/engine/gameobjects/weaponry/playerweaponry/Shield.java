package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingEnemyWeaponry;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.BulletEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.LaserBeamEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Shield extends GameObject {

    private float speed;
    private float radius;
    private int damage;
    private float direction;
    private float time;
    private float timeActivated;

    public Shield(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(GameObjectType.SHIELD, name, initialPosition, space);
        this.direction = direction;
        this.speed = speed;
        this.radius = radius;
        this.damage = damage;

        setCollisionRadius(radius);
        setHealth(100);
        setTimeActivated(20);

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Enemy){
            subject.setHealth(subject.getHealth() - getDamage());
            ((Enemy) subject).setDirection(((Enemy) subject).getDirection() + (float)Math.PI);
            setHealth(getHealth() - 10);
        }
        if(subject instanceof BulletEnemy){
            toDelete.add(subject);
        }
        if(subject instanceof HomingEnemyWeaponry){
            toDelete.add(subject);
        }
        if(subject instanceof LaserBeamEnemy){
            toDelete.add(subject);
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        time += delta;

        if(time >= timeActivated){
            toDelete.add(this);
            time =0;
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

    public void setRadius(float radius) {
        this.radius = radius;
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

    public float getTimeActivated() {
        return timeActivated;
    }

    public void setTimeActivated(float timeActivated) {
        this.timeActivated = timeActivated;
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

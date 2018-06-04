package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MinionShooterEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Bullet extends GameObject {

    private BulletState bulletState = BulletState.NONE;

    private float direction;
    private float radius;
    private float speed;
    private int damage;

    private float time;
    private boolean hit;


    public Bullet(String name, Vector2 initialPosition, SpaceEngine space, float direction,float speed, float radius, int damage) {
        super(GameObjectType.BULLET, name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;
        this.damage = damage;
        hit = false;

        setCollisionRadius(radius);
        setSpeed(speed);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (bulletState == BulletState.ATTACK_ENEMY) {
            if (subject instanceof Enemy) {
                toDelete.add(this);
                subject.setHealth(subject.getHealth() - getDamage());
                setHit(true);
            }
            if (subject instanceof MinionShooterEnemy) {
                toDelete.add(this);
                subject.setHealth(subject.getHealth() - getDamage());
                setHit(true);
            }
        }
        if (bulletState == BulletState.ATTACK_PLAYER){
            if(subject instanceof Player){
                toDelete.add(this);
                subject.setHealth(subject.getHealth() - getDamage());
                if (((Player) subject).isKilled()){
                    ((Player) subject).setKillerName(this.getName());
                }
            }
        }
    }

    public enum BulletState{
        NONE, ATTACK_PLAYER, ATTACK_ENEMY
    }

    private float bulletSpeed(){
        return speed = 500f;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float destructTime = 2.5f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

        switch (bulletState){
            case NONE:
                break;
            case ATTACK_PLAYER:
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * bulletSpeed() * delta,
                        getPosition().y + (float) Math.sin(direction) * bulletSpeed() * delta)
                );
                setOrientation(direction);
                break;
            case ATTACK_ENEMY:
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * bulletSpeed() * delta,
                        getPosition().y + (float) Math.sin(direction) * bulletSpeed() * delta)
                );
                setOrientation(direction);
                break;
        }
    }



    public float getDirection() {
        return direction;
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

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public BulletState getBulletState() {
        return bulletState;
    }

    public void setBulletState(BulletState bulletState) {
        this.bulletState = bulletState;
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

    @Override
    public Map<String, Object> getRandomProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("hit", hit);

        return result;
    }
}

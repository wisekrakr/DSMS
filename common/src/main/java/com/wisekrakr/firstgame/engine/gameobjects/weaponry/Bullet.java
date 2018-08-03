package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Bullet extends GameObject {

    private float direction;
    private float radius;
    private float speed;
    private double damage;

    private float time;
    private boolean hit;

    private float bulletSpeed;

    private boolean playerBullet;
    private boolean enemyBullet;

    public Bullet(String name, Vector2 initialPosition, float direction, float speed, float radius, double damage) {
        super(GameObjectVisualizationType.BULLET, name, initialPosition);
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
        if (playerBullet) {
            if (subject instanceof Enemy) {
                toDelete.add(this);
                subject.setHealth(subject.getHealth() - getDamage());
                setHit(true);
            }
            if (subject instanceof Minion) {
                if (((Minion) subject).isEnemyMinion()) {
                    toDelete.add(this);
                    subject.setHealth(subject.getHealth() - getDamage());
                    setHit(true);
                }
            }
            if (subject instanceof HomingMissile){
                if (((HomingMissile) subject).isEnemyMissile()){
                    toDelete.add(this);
                    toDelete.add(subject);
                    setHit(true);
                }
            }
            if (subject instanceof SpaceMine){
                if (((SpaceMine) subject).isEnemyMine()){
                    toDelete.add(this);
                    toDelete.add(subject);
                    setHit(true);
                }
            }
        }
        if (enemyBullet){
            if(subject instanceof Player){
                toDelete.add(this);
                subject.setHealth(subject.getHealth() - getDamage());
                if (((Player) subject).isKilled()){
                    ((Player) subject).setKillerName(this.getName());
                }
            }
            if (subject instanceof HomingMissile){
                if (((HomingMissile) subject).isPlayerMissile()){
                    toDelete.add(this);
                    toDelete.add(subject);
                    setHit(true);
                }
            }
            if (subject instanceof SpaceMine){
                if (((SpaceMine) subject).isPlayerMine()){
                    toDelete.add(this);
                    toDelete.add(subject);
                    setHit(true);
                }
            }
        }
    }




    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float destructTime = 2.5f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * bulletSpeed * delta,
                getPosition().y + (float) Math.sin(direction) * bulletSpeed * delta)
        );
        setOrientation(direction);

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

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isPlayerBullet() {
        return playerBullet;
    }

    public void setPlayerBullet(boolean playerBullet) {
        this.playerBullet = playerBullet;
    }

    public boolean isEnemyBullet() {
        return enemyBullet;
    }

    public void setEnemyBullet(boolean enemyBullet) {
        this.enemyBullet = enemyBullet;
    }

    public float getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);
        result.put("damage", damage);
        result.put("hit", hit);

        return result;
    }
}

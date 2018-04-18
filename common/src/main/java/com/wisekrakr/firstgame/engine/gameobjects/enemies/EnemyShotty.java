package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.BulletEnemy;

import java.util.Map;
import java.util.Set;

public class EnemyShotty extends Enemy {

    private static final int CHANGE_DIRECTION_TIME = 12;
    private float direction;
    private float radius;
    private int health;
    private float speed;
    private AttackState attackState = AttackState.PACIFIST;
    private int ammoCount;
    private float shotLeftOver;
    private float time;


    public EnemyShotty(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;

        ammoCount = (int) Double.POSITIVE_INFINITY;
        setCollisionRadius(radius);
        setHealth(health);
        setAggroDistance(1350);
        setAttackDistance(speed);
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            subject.setHealth(subject.getHealth() - 15);
        }
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {

            if (distanceBetween(this, target) <= getAggroDistance() ) {

                float angle = angleBetween(this, target);

                // to make the chaser chase the player with less vigilance, divide cos and sin by 2
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle)  , getPosition().y +=  Math.sin(angle)  ));

                setOrientation(angle);

                setDirection(angle);

            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {

            if (distanceBetween(this, target) <= getAttackDistance() ) {

                attackState = AttackState.SHOOT;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }


    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);
        time += delta;
        if(time >= CHANGE_DIRECTION_TIME){
            float randomDirection = setRandomDirection();
            setDirection(randomDirection);
            time=0;
        }

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * getSpeed() * delta,
                getPosition().y + (float) Math.sin(direction) * getSpeed() * delta)
        );
        setOrientation(direction);

        switch (attackState){

            case SHOOT:
                ammoCount = getAmmoCount();

                float shotCount = delta / 4.5f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), getDirection(), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /6), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /6), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /7), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /7), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /8), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /8), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /9), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /9), 400, 2f, randomDamageCountBullet()));

                }
                break;

            case PACIFIST:
                shotLeftOver = 0;
        }

    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }

    @Override
    public float getDirection() {
        return super.getDirection();
    }

    @Override
    public void setDirection(float direction) {
        super.setDirection(direction);
    }

    @Override
    public float getRadius() {
        return super.getRadius();
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        return super.getExtraSnapshotProperties();
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.signalOutOfBounds(toDelete, toAdd);
    }
}


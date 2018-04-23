package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.*;

public class EnemyMotherShip extends Enemy {

    private static final int CHANGE_DIRECTION_TIME = 3000;
    private float direction;
    private float radius;
    private int health;
    private float speed;
    private int ammoCount;

    private AttackState attackState = AttackState.PACIFIST;
    private float shotLeftOver;
    private float time;


    public EnemyMotherShip(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;

        ammoCount = 6;
        shotLeftOver = ammoCount;

        setCollisionRadius(radius);
        setHealth(health);
        setAggroDistance(1250);
        setAttackDistance(850);
        setSpeed(speed);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            subject.setHealth(subject.getHealth() - 20);
            toDelete.add(subject);
        }


    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {

            if (distanceBetween(this, target) <= getAggroDistance() ) {
                float angle = angleBetween(this, target);
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle) /2 , getPosition().y +=  Math.sin(angle)/2 ));
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

        setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * getSpeed() * delta,
                getPosition().y + (float) Math.sin(getDirection()) * getSpeed() * delta)
        );

        setOrientation(getDirection());

        switch (attackState){
            case SHOOT:
                ammoCount = getAmmoCount();
                float shotCount = delta / 1.5f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for(int i = 0; i < exactShotCount; i++) {
                    Random randomGenerator = new Random();
                    EnemyChaser enemyChaser = new EnemyChaser("ChaserMinion1", new Vector2(
                            getPosition().x + randomGenerator.nextFloat() * radius,
                            getPosition().y + randomGenerator.nextFloat() * radius),
                            8, getDirection(), 220,10f, getSpace());
                    toAdd.add(enemyChaser);

                    float destructTime = 8.0f;
                    time += delta;
                    if(time >= destructTime){
                        float angle = angleBetween(this, enemyChaser);

                        // to make the chaser chase the player with less vigilance, divide cos and sin by 2
                        enemyChaser.setPosition(new Vector2(this.getPosition().x +=  Math.cos(angle)  , this.getPosition().y +=  Math.sin(angle) ));

                        enemyChaser.setOrientation(angle);

                        enemyChaser.setDirection(angle);

                    }
                }

                break;

            case PACIFIST:
                shotLeftOver = 0;
        }



    }

    public int getAmmoCount() {
        return ammoCount;
    }

    @Override
    public float getDirection() {
        return direction;
    }

    @Override
    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getRadius() {
        return radius;
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
}

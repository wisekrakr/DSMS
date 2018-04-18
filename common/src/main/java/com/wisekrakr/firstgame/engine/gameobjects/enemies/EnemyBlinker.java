package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.LaserBeamEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class EnemyBlinker extends Enemy {

    private static final float CHANGE_DIRECTION_TIME = 20;
    private static final float BLINK_TIME = 5;
    private float direction;
    private float radius;
    private int health;
    private float speed;
    private float shotLeftOver;
    private int ammoCount;
    private float time;
    private AttackState attackState = AttackState.PACIFIST;

    public EnemyBlinker(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;

        ammoCount = (int) Double.POSITIVE_INFINITY;;
        shotLeftOver = ammoCount;

        setCollisionRadius(radius);
        setHealth(health);
        setAggroDistance(950);
        setAttackDistance(750);
        setSpeed(speed);

    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.signalOutOfBounds(toDelete, toAdd);
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
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle) /2 , getPosition().y +=  Math.sin(angle)/2 ));
                setOrientation(angle);
                setDirection(angle);
                attackState = AttackState.CHASE;

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
            Random random = new Random();
            setPosition(new Vector2(
                    getPosition().x + delta * getSpeed() * (random.nextFloat() * 300 - 100),
                    getPosition().y + delta * getSpeed() * (random.nextFloat() * 300 - 100)
            ));
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
                float shotCount = delta / 0.5f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(new LaserBeamEnemy("laser", getPosition(), getSpace(), getOrientation(), 2f, randomDamageCountBullet()));
                }

                break;

            case CHASE:
                if(time >= BLINK_TIME) {

                    Random random = new Random();
                    setPosition(new Vector2(
                            getPosition().x + delta * getSpeed() * (random.nextFloat() * 200 - 100),
                            getPosition().y + delta * getSpeed() * (random.nextFloat() * 200 - 100)
                    ));
                    time = 0;
                }
                break;
            case PACIFIST:
                shotLeftOver = 0;
                break;
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


}



package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.SpaceMineEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnemyShitter extends Enemy {

    private static final float DEFAULT_ENEMY_SPEED = 150;
    private static final float CHANGE_DIRECTION_TIME = 16;
    private float direction;
    private float radius;
    private int health;
    private float speed;
    private float shotLeftOver;
    private float minesLeft;
    private int ammoCount;
    private int minesCount;
    private float time;
    private AttackState attackState = AttackState.PACIFIST;

    public EnemyShitter(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;

        minesCount = 1000;
        minesLeft = minesCount;

        setCollisionRadius(radius);
        setHealth(health);
        setAggroDistance(450);
        setAttackDistance(800);
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
                setPosition(new Vector2(getPosition().x -=  Math.cos(angle), getPosition().y -=  Math.sin(angle)));
                setOrientation(-angle);
                setDirection(-angle);

            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance() ) {

                attackState = AttackState.SHOOT;
            }
            else{
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
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_ENEMY_SPEED * delta)
        );
        setOrientation(direction);

        switch (attackState){
            case SHOOT:
                minesCount = getMinesCount();
                float minesShotCount = delta / 2.0f + minesLeft;

                int exactMinesShotCount = Math.min(Math.round(minesShotCount), minesCount);

                minesCount = minesCount - exactMinesShotCount;
                if (minesCount > 0) {
                    minesLeft = minesShotCount - exactMinesShotCount;
                } else {
                    minesLeft = 0;
                }

                for (int i = 0; i < exactMinesShotCount; i++) {
                    toAdd.add(new SpaceMineEnemy("enemy_mine", getPosition(), getSpace(), getOrientation(), 300, 8f, randomDamageCountMine()));
                }

                break;


            case PACIFIST:
                shotLeftOver = 0;
                minesLeft = 0;
                break;
        }

    }


    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public void setMinesCount(int minesCount) {
        this.minesCount = minesCount;
    }

    public float getDirection() {
        return direction;
    }

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

package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BulletEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.MissilePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * EnemyPest is an Enemy that will clone a smaller version of itself when it gets hit with a missile. So kill it with something else!
 */

public class EnemyPest extends Enemy {
    private static final float DEFAULT_ENEMY_SPEED = 220;
    private static final float AGRO_DISTANCE = 950;
    private static final float ATTACK_DISTANCE = 750;
    private static final float CHANGE_DIRECTION_TIME = 10;
    private float direction;
    private float radius;
    private int health;
    private float shotLeftOver;
    private float clonesLeftOver;
    private int ammoCount;
    private int cloneCount;
    private float time;
    private AttackState attackState = AttackState.PACIFIST;

    public EnemyPest(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(name, position, health, direction, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;

        ammoCount = (int) Double.POSITIVE_INFINITY;;
        shotLeftOver = ammoCount;

        cloneCount = 1000;
        cloneCount = (int) clonesLeftOver;

        setCollisionRadius(radius);
        setHealth(health);

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
        if(subject instanceof MissilePlayer){
            Random random = new Random();
            toAdd.add(new EnemyPest("pesty", this.getPosition(), 10, getOrientation(), 10f, getSpace()));
        }

    }

    @Override
    public void targetSpotted(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player) {
            if (distanceBetween(this, subject) <= AGRO_DISTANCE ) {
                float angle = angleBetween(this, subject);
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle), getPosition().y +=  Math.sin(angle)));
                setOrientation(angle);
                setDirection(angle);

            }
        }
    }

    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(subject, toDelete, toAdd);

        if (subject instanceof Player) {
            if (distanceBetween(this, subject) <= ATTACK_DISTANCE ) {
                attackState = AttackState.SHOOT;
            }
            else{
                attackState = AttackState.PACIFIST;
            }
        }
    }



    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        super.elapseTime(delta, toDelete, toAdd);

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
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), getOrientation(), 400, 2f));
                }

                break;

            case PACIFIST:
                shotLeftOver = 0;
                clonesLeftOver = 0;
                break;
        }

    }


    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }

    public int getCloneCount() {
        return cloneCount;
    }

    public void setCloneCount(int cloneCount) {
        this.cloneCount = cloneCount;
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

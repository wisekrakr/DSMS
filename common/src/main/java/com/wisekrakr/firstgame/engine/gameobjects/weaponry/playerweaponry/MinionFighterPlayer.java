package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.BulletEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.LaserBeamEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MissileEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.SpaceMineEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MinionFighterPlayer extends Minion {

    private MinionState minionState = MinionState.PACIFIST;

    private static final float ATTACK_DISTANCE = 700;
    private static final float SPOTTED_DISTANCE = 900;
    private static final float SPEED = 100;
    private float direction;
    private float radius;
    private int health;

    private float time;
    private int damage;

    public MinionFighterPlayer(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(name, position, health, direction, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;

        setCollisionRadius(radius);
        setHealth(health);

        damage = 10;

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Enemy){
            subject.setHealth(subject.getHealth() - getDamage());
        }
        if (subject instanceof BulletEnemy){
            setHealth(getHealth() - subject.randomDamageCountBullet());
        }
        if (subject instanceof MissileEnemy){
            setHealth(getHealth() - subject.randomDamageCountMissile());
        }
        if (subject instanceof LaserBeamEnemy){
            setHealth(getHealth() - subject.randomDamageCountBullet());
        }
        if (subject instanceof SpaceMineEnemy){
            setHealth(getHealth() - subject.randomDamageCountMine());
        }



    }


    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(target instanceof Enemy) {
            if(distanceBetween(this, target)<= ATTACK_DISTANCE) {
                float angle = angleBetween(this, target);
            /*
                setPosition(new Vector2(getPosition().x += (getCollisionRadius() * 2) * (float) Math.cos(getOrientation()),
                    getPosition().y += (getCollisionRadius() * 2) * (float) Math.sin(getOrientation())));
             */
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle) *3 , getPosition().y +=  Math.sin(angle)*3 ));

                setOrientation(angle);
                setDirection(angle);
                minionState = MinionState.SHOOT;
            }

        }

    }
    

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);

        switch (minionState){
            case SHOOT:

                break;

            case PACIFIST:


                break;
        }
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }


    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public MinionState getMinionState() {
        return minionState;
    }

    public void setMinionState(MinionState minionState) {
        this.minionState = minionState;
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

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }

}

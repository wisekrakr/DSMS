package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MineMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;
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
    private static final float SPEED = 300;
    private float direction;
    private float radius;
    private int health;

    private float time;
    private int damage;

    public MinionFighterPlayer(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(GameObjectType.MINION_FIGHTER, name, position, health, direction, radius, space);
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
            setHealth(getHealth() - BulletMechanics.determineBulletDamage());
        }
        if (subject instanceof MissileEnemy){
            setHealth(getHealth() - MissileMechanics.determineMissileDamage());
        }
        if (subject instanceof LaserBeamEnemy){
            setHealth(getHealth() - BulletMechanics.determineBulletDamage());
        }
        if (subject instanceof SpaceMineEnemy){
            setHealth(getHealth() - MineMechanics.determineMineDamage());
        }



    }


    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(target instanceof Enemy) {
            if(distanceBetween(this, target)<= SPOTTED_DISTANCE) {
                float angle = angleBetween(this, target);
                setOrientation(angle);
                setDirection(angle);
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Enemy){
            if (distanceBetween(this, target)<= ATTACK_DISTANCE){
                if (!toDelete.contains(target)) {
                    minionState = MinionState.SHOOT;
                }else {
                    minionState = MinionState.PACIFIST;
                }
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);

        switch (minionState){
            case SHOOT:

                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * SPEED * delta,
                        getPosition().y + (float) Math.sin(direction) * SPEED * delta)
                );
                setOrientation(direction);

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

        System.out.println("Radius = " + radius);
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

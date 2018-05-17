package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BulletMisc;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.BulletEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.LaserBeamEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MissileEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.SpaceMineEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MinionShooterPlayer extends Minion {

    private MinionState minionState = MinionState.PACIFIST;

    private static final float ATTACK_DISTANCE = 700;
    private static final float SPOTTED_DISTANCE = 900;

    private float direction;
    private float radius;
    private int health;
    private float shotLeftOver;
    private int ammoCount;
    private int damage;

    public MinionShooterPlayer(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(GameObjectType.MINION_SHOOTER, name, position, health, direction, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;

        ammoCount = (int) Double.POSITIVE_INFINITY;;
        shotLeftOver = ammoCount;

        setCollisionRadius(radius);
        setHealth(health);

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
            setHealth(getHealth() - subject.randomDamageCountMissile());
        }
        if (subject instanceof LaserBeamEnemy){
            setHealth(getHealth() - BulletMechanics.determineBulletDamage());
        }
        if (subject instanceof SpaceMineEnemy){
            setHealth(getHealth() - subject.randomDamageCountMine());
        }
    }

    @Override
    public void minionBounds(GameObject object, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(object instanceof Player){

        }
    }

    //TODO: Bug// Minion either keeps shooting or only shoots one random enemy sometimes
    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(target instanceof Enemy) {
            if(distanceBetween(this, target)<= SPOTTED_DISTANCE) {
                float angle = angleBetween(this, target);
            /*
            setPosition(new Vector2(getPosition().x += (getCollisionRadius() * 2) * (float) Math.cos(getOrientation()),
                    getPosition().y += (getCollisionRadius() * 2) * (float) Math.sin(getOrientation())));
                    */
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
                ammoCount = getAmmoCount();

                float shotCount = delta / 0.2f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(new BulletMisc("bullito", getPosition(), getSpace(), getDirection(), 400, 2f, BulletMechanics.determineBulletDamage()));
                }

                break;

            case PACIFIST:
                shotLeftOver = 0;
                break;
            case RETURN:


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

    public int getAmmoCount() {
        return ammoCount;
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

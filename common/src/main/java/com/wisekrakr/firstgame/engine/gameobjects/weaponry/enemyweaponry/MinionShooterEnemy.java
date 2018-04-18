package com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.Shield;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.BulletPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.MissilePlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.SpaceMinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MinionShooterEnemy extends Minion {

    private MinionState minionState = MinionState.PACIFIST;

    private static final float ATTACK_DISTANCE = 700;
    private static final float SPOTTED_DISTANCE = 900;
    private static final float SPEED = 100;
    private float direction;
    private float radius;
    private int health;
    private float shotLeftOver;
    private int ammoCount;
    private float time;
    private int damage;
    private BulletEnemy bulletMisc;

    public MinionShooterEnemy(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(name, position, health, direction, radius, space);
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
        if (subject instanceof Player){
            subject.setHealth(subject.getHealth() - getDamage());
        }
        if (subject instanceof BulletPlayer){
            setHealth(getHealth() - subject.randomDamageCountBullet());
        }
        if (subject instanceof MissilePlayer){
            setHealth(getHealth() - subject.randomDamageCountMissile());
        }
        if (subject instanceof Shield){
            setHealth(getHealth() - 25);
        }
        if (subject instanceof SpaceMinePlayer){
            setHealth(getHealth() - subject.randomDamageCountMine());
        }
    }

    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(target instanceof Player) {
            if(distanceBetween(this, target)<= SPOTTED_DISTANCE) {
                float angle = angleBetween(this, target);
                setOrientation(angle);
                setDirection(angle);
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player){
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

        setPosition(new Vector2((float) (getPosition().x + Math.PI * 3 * 120 * delta),
                (float) (getPosition().y + Math.PI * 3 * 120 * delta))
        );

        switch (minionState){
            case SHOOT:
                ammoCount = getAmmoCount();

                float shotCount = delta / 0.8f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    bulletMisc = new BulletEnemy("bullito", getPosition(), getSpace(), getOrientation(), 400, 2f, randomDamageCountBullet());
                    toAdd.add(bulletMisc);
                }

                break;

            case PACIFIST:
                shotLeftOver = 0;
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

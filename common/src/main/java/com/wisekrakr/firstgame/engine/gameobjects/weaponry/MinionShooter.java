package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.AutonomousWeaponsPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MinionShooter extends AutonomousWeaponsPlayer {

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
    private BulletMisc bulletMisc;

    public MinionShooter(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(name, position, space, direction, radius);
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
        super.collide(subject, toDelete, toAdd);
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

    public enum MinionState {
        PACIFIST, SHOOT
    }
//TODO: Minion either keeps shooting or only shoots one random enemy
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
                minionState = MinionState.SHOOT;
            }
        }
    }
// attackTarget not used for now.
    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Enemy) {
            if (distanceBetween(this, subject) <= ATTACK_DISTANCE ) {
                minionState = MinionState.SHOOT;
            }else {
                minionState = MinionState.PACIFIST;
            }
        }
    }
    
    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (health <= 0){
            toDelete.add(this);
        }

        float destructTime = 30f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

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
                    bulletMisc = new BulletMisc("bullito", getPosition(), getSpace(), getOrientation(), 400, 2f);
                    toAdd.add(bulletMisc);
                    bulletMisc.setDamage(bulletMisc.randomDamageCountBullet());
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

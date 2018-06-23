package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MineMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Minion extends GameObject {

    private MinionAttackState minionAttackState = MinionAttackState.PACIFIST;

    private float aggroDistance;
    private float attackDistance;

    private float direction;
    private float radius;
    private int health;
    private float speed;
    private Vector2 targetVector;

    private float time;
    private int damage;
    private float destructTime;
    private boolean destruct;

    private int ammoCount;
    private float shotLeftOver;

    private boolean playerMinion;
    private boolean enemyMinion;
    private boolean minionFighter;
    private boolean minionShooter;

    public Minion(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(GameObjectType.MINION, name, position, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;

        setCollisionRadius(radius);
        setHealth(health);

        setAggroDistance(175f);
        setAttackDistance(150f);

        damage = 10;
        speed = 400f;
    }

    public enum MinionAttackState {
        PACIFIST, SHOOT, FIGHT, RETURN
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (playerMinion) {
            if (minionAttackState == MinionAttackState.SHOOT || minionAttackState == MinionAttackState.FIGHT) {
                if (subject instanceof Enemy) {
                    subject.setHealth(subject.getHealth() - getDamage());
                }
                if (subject instanceof Bullet) {
                    setHealth(getHealth() - BulletMechanics.determineBulletDamage());
                }
                if (subject instanceof HomingMissile) {
                    setHealth(getHealth() - MissileMechanics.determineMissileDamage());
                }
                if (subject instanceof LaserBeam) {
                    setHealth(getHealth() - BulletMechanics.determineBulletDamage());
                }
                if (subject instanceof SpaceMine) {
                    setHealth(getHealth() - MineMechanics.determineMineDamage());
                }
            }
        }
        if (enemyMinion) {
            if (minionAttackState == MinionAttackState.SHOOT || minionAttackState == MinionAttackState.FIGHT) {
                if (subject instanceof Player) {
                    subject.setHealth(subject.getHealth() - getDamage());
                    if (((Player) subject).isKilled()) {
                        ((Player) subject).setKillerName(this.getName());
                    }
                }
                if (subject instanceof Bullet) {
                    setHealth(getHealth() - BulletMechanics.determineBulletDamage());
                }
                if (subject instanceof HomingMissile) {
                    setHealth(getHealth() - MissileMechanics.determineMissileDamage());
                }
                if (subject instanceof Shield) {
                    setHealth(getHealth() - 25);
                }
                if (subject instanceof SpaceMine) {
                    setHealth(getHealth() - MineMechanics.determineMineDamage());
                }
            }
        }
    }

    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (playerMinion) {
            if (target instanceof Enemy) {
                if (distanceBetween(this, target) <= getAggroDistance()) {
                    if (minionShooter || minionFighter) {
                        float angle = angleBetween(this, target);
                        setOrientation(angle);
                        setDirection(angle);
                    }else {
                        minionAttackState = MinionAttackState.PACIFIST;
                    }
                }
            }
        }
        if (enemyMinion) {
            if (target instanceof Player) {
                if (minionShooter || minionFighter) {
                    if (distanceBetween(this, target) <= getAggroDistance()) {
                        float angle = angleBetween(this, target);
                        setOrientation(angle);
                        setDirection(angle);
                    }else {
                        minionAttackState = MinionAttackState.PACIFIST;
                    }
                }
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (playerMinion) {
            if (target instanceof Enemy) {
                if (distanceBetween(this, target) <= getAttackDistance()) {
                    if (minionShooter) {
                        if (!toDelete.contains(target)) {
                            setMinionAttackState(MinionAttackState.SHOOT);
                        } else {
                            setMinionAttackState(MinionAttackState.PACIFIST);
                        }
                    }
                    if (minionFighter){
                        if (!toDelete.contains(target)) {
                            setMinionAttackState(MinionAttackState.FIGHT);
                        } else {
                            setMinionAttackState(MinionAttackState.PACIFIST);

                        }
                    }
                }
            }
        }

        if (enemyMinion){
            if (target instanceof Player){
                if (distanceBetween(this, target)<= getAttackDistance()){
                    if (minionShooter) {
                        if (!toDelete.contains(target)) {
                            setMinionAttackState(MinionAttackState.SHOOT);
                        } else {
                            setMinionAttackState(MinionAttackState.PACIFIST);
                        }
                    }
                }
            }
        }

    }


    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (health <= 0){
            toDelete.add(this);
        }

        destructTime = 30f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
            destruct = true;
        }

        switch (minionAttackState){
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
                    Bullet bullet = new Bullet("bullito", getPosition(), getSpace(), getOrientation(), getSpeed(),
                            BulletMechanics.radius(1), BulletMechanics.determineBulletDamage());
                    toAdd.add(bullet);
                    bullet.setBulletSpeed(getSpeed() * 3);
                    if (enemyMinion) {
                        bullet.setPlayerBullet(true);
                    }
                    if (playerMinion){
                        bullet.setEnemyBullet(true);
                    }
                }
                break;

            case FIGHT:
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * getSpeed() * delta,
                        getPosition().y + (float) Math.sin(direction) * getSpeed() * delta)
                );
                setOrientation(direction);
                break;
            case PACIFIST:
                shotLeftOver = 0;
                break;
            case RETURN:
                setPosition(new Vector2(targetVector.x + (float) Math.cos(direction) * getSpeed() * delta,
                        targetVector.y + (float) Math.sin(direction) * getSpeed() * delta)
                );
                setOrientation(direction);

                break;
        }

    }

    public boolean isMinionFighter() {
        return minionFighter;
    }

    public void setMinionFighter(boolean minionFighter) {
        this.minionFighter = minionFighter;
    }

    public boolean isMinionShooter() {
        return minionShooter;
    }

    public void setMinionShooter(boolean minionShooter) {
        this.minionShooter = minionShooter;
    }

    public boolean isPlayerMinion() {
        return playerMinion;
    }

    public void setPlayerMinion(boolean playerMinion) {
        this.playerMinion = playerMinion;
    }

    public boolean isEnemyMinion() {
        return enemyMinion;
    }

    public void setEnemyMinion(boolean enemyMinion) {
        this.enemyMinion = enemyMinion;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isDestruct() {
        return destruct;
    }

    public MinionAttackState getMinionAttackState() {
        return minionAttackState;
    }

    public void setMinionAttackState(MinionAttackState minionAttackState) {
        this.minionAttackState = minionAttackState;
    }

    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getAggroDistance() {
        return aggroDistance;
    }

    public void setAggroDistance(float aggroDistance) {
        this.aggroDistance = aggroDistance;
    }

    public float getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(float attackDistance) {
        this.attackDistance = attackDistance;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2 getTargetVector() {
        return targetVector;
    }

    public void setTargetVector(Vector2 targetVector) {
        this.targetVector = targetVector;
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

    @Override
    public Map<String, Object> getRandomProperties() {
        Map<String, Object> result = new HashMap<>();

        if (minionShooter) {
            result.put("minionshooter", minionShooter);
        }
        if (minionFighter){
            result.put("minionfighter", minionFighter);
        }
        return result;
    }
}

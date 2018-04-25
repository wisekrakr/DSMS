package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Spores;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.BulletEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.LaserBeamEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MissileEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.SpaceMineEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Enemy extends GameObject {

    private float direction;
    private float radius;
    private int health;
    private float speed;
    private float attackDistance;
    private float aggroDistance;
    private float time;
    private float changeDirectionTime;

    private static final float CLOSE_RANGE = 100;
    private static final float CLOSEST_TARGET = 100;

    private AttackState attackState = AttackState.PACIFIST;

    private int ammoCount;
    private float shotLeftOver;
    private int missileAmmoCount;
    private float missilesShotLeftOver;
    private int shottyAmmoCount;
    private float shottyShotLeftOver;
    private int sporesAmmoCount;
    private float sporesShotLeftOver;
    private int childrenAmmoCount;
    private float childrenShotLeftOver;
    private int laserAmmoCount;
    private float laserShotLeftOver;
    private int minesCount;
    private float minesLeft;


    public Enemy(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;
        missileAmmoCount = (int) Double.POSITIVE_INFINITY;
        missilesShotLeftOver = missileAmmoCount;
        shottyAmmoCount = (int) Double.POSITIVE_INFINITY;
        shottyShotLeftOver = shottyAmmoCount;
        sporesAmmoCount = (int) Double.POSITIVE_INFINITY;
        sporesShotLeftOver = sporesAmmoCount;
        childrenAmmoCount = 6;
        childrenShotLeftOver = childrenAmmoCount;
        laserAmmoCount = (int) Double.POSITIVE_INFINITY;
        laserShotLeftOver = laserAmmoCount;
        minesCount = (int) Double.POSITIVE_INFINITY;
        minesLeft = minesCount;

        setHealth(health);
        setCollisionRadius(radius);
        setSpeed(speed);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
       this.setDirection(this.getDirection() + (float) Math.PI * this.getOrientation());
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            subject.setHealth(subject.getHealth() - 15);
        }
    }

    /*This method counts as a way to prevent enemies from overlapping each other while attacking or flying towards one
    * another.*/

    @Override
    public void overlappingObjects(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        Random random = new Random();
        if(subject instanceof Enemy){
            float angle = angleBetween(this, subject);
            if(distanceBetween(this, subject)<= getCollisionRadius() + subject.getCollisionRadius()) {
                setPosition(new Vector2(getPosition().x -= Math.cos(angle) * random.nextFloat() * 2.5,
                        getPosition().y -= Math.sin(angle) * random.nextFloat() * 2.5));
                setOrientation(-angle);
                setDirection(direction + (float) Math.PI);
            }else{
                toDelete.add(this);
            }
        }
    }

    /*Every way to attack a player. Switch and cases are below in the elapsedTime() method
    * The CHASE case is in every children class respectively */

    public enum AttackState {
        PACIFIST, CHASE, FIRE_BULLETS, FIRE_MISSILES, FIRE_MINES, FIRE_CHILDREN, FIRE_MINIONS, FIRE_SPORES, FIRE_SHOTGUN,
        FIRE_LASER, SELF_DESTRUCT;
    }


/*
    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(target instanceof Player){
            if(distanceBetween(this, target)< CLOSEST_TARGET){
                attackTarget(target, toDelete, toAdd);
            }
        }
    }
*/


    /*For most enemies this method counts as a way to move towards the player.
    * Some children classes have their own @Override of this method for a different action.*/

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAggroDistance()  ) {
                float angle = angleBetween(this, target);
                float angleNoAim = angleBetweenNoAim(this, target);
                setPosition(new Vector2(getPosition().x  +=  Math.cos(angle), getPosition().y += Math.sin(angle) ));
                setOrientation(angle);
                setDirection(angleNoAim);
                /*
                if (distanceBetween(this, target) >= CLOSE_RANGE){
                    setPosition(new Vector2(getPosition().x  -=  Math.cos(angle), getPosition().y -= Math.sin(angle)));
                }
                */
            }

        }
    }

    /* This is where every different enemy gets its action when it is in attackdistance from the Player.
    * Most enemies have the same basic action (shooting a Player).
    * Some have there own @Override of this method for additional actions*/

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance() ) {
                if (this instanceof EnemyShitter) {
                    attackState = AttackState.FIRE_MINES;
                }else if (this instanceof EnemyHomer) {
                    attackState = AttackState.FIRE_MISSILES;
                }else if (this instanceof EnemyShotty){
                    attackState = AttackState.FIRE_SHOTGUN;
                }else if (this instanceof EnemyMutator) {
                    attackState = AttackState.FIRE_SPORES;
                }else if (this instanceof EnemyMotherShip) {
                    attackState = AttackState.FIRE_CHILDREN;
                }else if (this instanceof EnemyBlinker) {
                    attackState = AttackState.FIRE_LASER;
                }else {
                    attackState = AttackState.FIRE_BULLETS;
                }
            }else if (target.getHealth() <= 0){
                attackState = AttackState.PACIFIST;
            }
            else{
                attackState = AttackState.PACIFIST;
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * getSpeed() * delta,
                getPosition().y + (float) Math.sin(direction) * getSpeed() * delta)
        );
        setOrientation(direction);

        switch (attackState) {
            case FIRE_BULLETS:
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
                    toAdd.add(new BulletEnemy("enemybullito", getPosition(), getSpace(), getOrientation(), getSpeed(),
                            2f, randomDamageCountBullet()));
                }

                break;

            case FIRE_MISSILES:
                missileAmmoCount = getMissileAmmoCount();
                float missileShotCount = delta / 1.5f + missilesShotLeftOver;
                int missileExactShotCount = Math.min(Math.round(missileShotCount), missileAmmoCount);

                missileAmmoCount = missileAmmoCount - missileExactShotCount;
                if (missileAmmoCount > 0) {
                    missilesShotLeftOver = missileShotCount - missileExactShotCount;
                } else {
                    missilesShotLeftOver = 0;
                }

                for (int i = 0; i < missileExactShotCount; i++) {
                    toAdd.add(new MissileEnemy("enemymissile", new Vector2(getPosition().x + 16, getPosition().y + 16),
                            getSpace(), getOrientation(), 300,5f, randomDamageCountMissile()));
                }

                break;

            case FIRE_SHOTGUN:
                shottyAmmoCount = getShottyAmmoCount();
                float shottyShotCount = delta / 4.5f + shottyShotLeftOver;
                int exactShottyShotCount = Math.min(Math.round(shottyShotCount), shottyAmmoCount);

                shottyAmmoCount = shottyAmmoCount - exactShottyShotCount;
                if (shottyAmmoCount > 0) {
                    shottyShotLeftOver = shottyShotCount - exactShottyShotCount;
                } else {
                    shottyShotLeftOver = 0;
                }

                for (int i = 0; i < exactShottyShotCount; i++) {
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), getDirection(), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /6), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /6), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /7), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /7), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /8), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /8), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /9), 400, 2f, randomDamageCountBullet()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /9), 400, 2f, randomDamageCountBullet()));

                }
                break;

            case FIRE_SPORES:
                sporesAmmoCount = getSporesAmmoCount();
                float sporesShotCount = delta / 0.1f + sporesShotLeftOver;

                int sporesExactShotCount = Math.min(Math.round(sporesShotCount), sporesAmmoCount);

                sporesAmmoCount = sporesAmmoCount - sporesExactShotCount;
                if (sporesAmmoCount > 0) {
                    sporesShotLeftOver = sporesShotCount - sporesExactShotCount;
                } else {
                    sporesShotLeftOver = 0;
                }

                for (int i = 0; i < sporesExactShotCount; i++) {
                    Random randomGenerator = new Random();
                    toAdd.add(new Spores("spores", new Vector2(getPosition().x + randomGenerator.nextFloat() * 100f,
                            getPosition().y + randomGenerator.nextFloat() * 100f),
                            getSpace(), getOrientation() + randomGenerator.nextFloat() * 100f, 200,1f, randomDamageCountBullet() / 5));
                }

                break;

            case FIRE_CHILDREN:
                childrenAmmoCount = getChildrenAmmoCount();
                float childrenShotCount = delta / 1.5f + childrenShotLeftOver;

                int childrenExactShotCount = Math.min(Math.round(childrenShotCount), childrenAmmoCount);

                childrenAmmoCount = childrenAmmoCount - childrenExactShotCount;
                if (childrenAmmoCount > 0) {
                    childrenShotLeftOver = childrenShotCount - childrenExactShotCount;
                } else {
                    childrenShotLeftOver = 0;
                }

                for(int i = 0; i < childrenExactShotCount; i++) {
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
                        enemyChaser.setPosition(new Vector2(this.getPosition().x +=  Math.cos(angle)  , this.getPosition().y +=  Math.sin(angle) ));
                        enemyChaser.setOrientation(angle);
                        enemyChaser.setDirection(angle);
                    }
                }
                break;

            case FIRE_LASER:
                laserAmmoCount = getLaserAmmoCount();
                float laserShotCount = delta / 0.5f + laserShotLeftOver;

                int laserExactShotCount = Math.min(Math.round(laserShotCount), laserAmmoCount);

                laserAmmoCount = laserAmmoCount - laserExactShotCount;
                if (laserAmmoCount > 0) {
                    laserShotLeftOver = laserShotCount - laserExactShotCount;
                } else {
                    laserShotLeftOver = 0;
                }

                for (int i = 0; i < laserExactShotCount; i++) {
                    toAdd.add(new LaserBeamEnemy("laser", getPosition(), getSpace(), getOrientation(), 2f, randomDamageCountBullet()));
                }

                break;

            case FIRE_MINES:
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

                time += delta;
                if(time >= getChangeDirectionTime()){
                    float randomDirection = setRandomDirection();
                    setDirection(randomDirection);
                    time=0;
                }

                shotLeftOver = 0;
                missilesShotLeftOver = 0;
                shottyShotLeftOver = 0;
                sporesShotLeftOver = 0;
                childrenShotLeftOver = 0;
                laserShotLeftOver = 0;
                minesLeft = 0;
                break;
        }

        if (health <= 0) {
            toDelete.add(this);
            Random random = new Random();
            int debrisParts = random.nextInt(10) + 1;
            for (int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", this.getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 30, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getRadius()));

            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(float attackDistance) {
        this.attackDistance = attackDistance;
    }

    public float getAggroDistance() {
        return aggroDistance;
    }

    public void setAggroDistance(float aggroDistance) {
        this.aggroDistance = aggroDistance;
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
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getChangeDirectionTime() {
        return changeDirectionTime;
    }

    public void setChangeDirectionTime(float changeDirectionTime) {
        this.changeDirectionTime = changeDirectionTime;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public int getMissileAmmoCount() {
        return missileAmmoCount;
    }

    public int getShottyAmmoCount() {
        return shottyAmmoCount;
    }

    public int getSporesAmmoCount() {
        return sporesAmmoCount;
    }

    public int getChildrenAmmoCount() {
        return childrenAmmoCount;
    }

    public int getLaserAmmoCount() {
        return laserAmmoCount;
    }

    public int getMinesCount() {
        return minesCount;
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

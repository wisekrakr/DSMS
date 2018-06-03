package com.wisekrakr.firstgame.engine.gameobjects.enemies;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MineMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BulletMisc;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Spores;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.*;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.BulletPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.MissilePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class Enemy extends GameObject {

    private float direction;
    private float radius;
    private float health;
    private float speed;
    private float attackDistance;
    private float aggroDistance;
    private float time;
    private float changeDirectionTime;
    private Vector2 targetVector;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;

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

    private float randomAngle;
    private float updatedAngle;
    private float rotationAngle;
    private double minionAngle;
    private MinionShooterEnemy minionShooterEnemy;
    private EnemyGang enemyGang;
    private float gangAngle;

    private float damageTaken;

    public Enemy(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(type, name, position, space);
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
        damageTaken = 0;

        setHealth(health);
        setCollisionRadius(radius);
        setSpeed(speed);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
       this.setDirection(this.getDirection() + (float) Math.PI);
    }

    /* These are the methods to initialize a MinionShooter for an enemy. Implement the methods in an enemy class. Example = EnemyEls*/

    public Minion initMinion(){
        minionShooterEnemy = new MinionShooterEnemy("minion", new Vector2(getPosition().x + getCollisionRadius() * 2,
                getPosition().y + getCollisionRadius() * 2), 20, getOrientation(), 8f, getSpace());
        return minionShooterEnemy;
    }

    public void minionMovement(float delta){
        minionAngle += 2f * delta;
        minionShooterEnemy.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 45f),
                (float) (getPosition().y + Math.sin(minionAngle) * 45f)));
    }

    public EnemyGang initGang(){
        enemyGang = new EnemyGang("Boi", new Vector2(getPosition().x + getCollisionRadius() * 2,
                getPosition().y + getCollisionRadius() * 2), 20, getOrientation(), 120f, 10f, getSpace());
        return enemyGang;
    }

    public void gangMovement(float delta){
        gangAngle += 2f * delta;
        enemyGang.setPosition(new Vector2((float) (getTargetVector().x + Math.cos(minionAngle) * 45f),
                (float) (getTargetVector().y + Math.sin(minionAngle) * 45f)));
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            subject.setHealth(subject.getHealth() - 15);
            setMovingState(MovingState.BACKWARDS);
            if (((Player) subject).isKilled()){
                ((Player) subject).setKillerName(this.getName());
            }

        }
        if (subject instanceof BulletPlayer || subject instanceof MissilePlayer || subject instanceof BulletMisc) {
            float angle = angleBetween(this, subject);
            setMovingState(MovingState.DEFAULT_FORWARDS);
            setOrientation(angle);
            setDirection(angle);
            damageTaken = subject.getDamage();
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
        PACIFIST, BLINK, FIRE_BULLETS, FIRE_MISSILES, FIRE_MINES, FIRE_CHILDREN, GANG_VOILENCE, FIRE_SPORES, FIRE_SHOTGUN,
        FIRE_LASER, SELF_DESTRUCT
    }

    public enum MovingState {
        FROZEN, DEFAULT_FORWARDS, BACKWARDS, DODGING, FLY_AROUND, FLY_BY, FACE_HUGGING
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
    * Some children classes have their own @Override of this method for a different action.
    * -If this enemy's health is more than 10% of its health it will get an Orientation and Direction towards the target
    * which is always the player
    * -Else it will no longer shoot and move away from the player.*/

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAggroDistance()) {
                float angle = angleBetween(this, target);
                float angleNoAim = angleBetweenNoAim(this, target);
                //setPosition(new Vector2(getPosition().x  +=  Math.cos(angle), getPosition().y += Math.sin(angle) ));
                if (!(getHealth() <= getHealth()*(10f/100f))){
                    setMovingState(getMovingState());
                    setOrientation(angle);
                    setDirection(angleNoAim);
                }else {
                    setMovingState(MovingState.BACKWARDS);
                    setAttackState(AttackState.PACIFIST);
                }
            }
        }
    }

    /* This is where every different enemy gets its action when it is in attackDistance from the Player.
    * Most enemies have the same basic action (shooting a Player).
    * Every child class has its specified way of attacking. Go to child class to see what attack each individual enemy has*/

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    /*If the Player is dead, stop shooting and just fly around
    * */
        if (target instanceof Player) {
            if (target.getHealth() <= 0) {
                attackState = AttackState.PACIFIST;
            }
        }
    }

    private float updateAngle(float delta){
        time += delta;

        if (time >= getChangeDirectionTime()) {
            updatedAngle = (float) (45 * Math.PI * delta);
            time = 0;
        }

        return updatedAngle;
    }

    private float randomAngle(float delta){
        time += delta;
        Random random = new Random();

        if (time >= getChangeDirectionTime()) {
            randomAngle = (float) (random.nextInt(180) * Math.PI * delta);
            time = 0;
        }
        return randomAngle;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        Random random = new Random();

        /*If the enemy has an float of less than 0 health, the enemy gets destroyed and debris GameObjects are spawned in its place
         * */
        if (health <= 0) {
            setAttackState(AttackState.SELF_DESTRUCT);
        }

        /*These are the ways an Enemy can move. Different kinds of movements for different occasions.
        * -DEFAULT_FORWARDS sets the enemy to move towards the direction it has been given
        * -BACKWARDS sets the enemy to move backwards with the opposite direction it has been given
        * -DODGING sets a different angle towards the player (handled in children class in attackTarget method.*/

        switch (movingState) {
            case DEFAULT_FORWARDS:
                /* Here we give every individual enemy its own random direction change. setChangeDirectionTime is handled in each individual enemy class
                 * */
                time += delta;
                if(time >= getChangeDirectionTime()){
                    float randomDirection = setRandomDirection();
                    setDirection(randomDirection);
                    time = 0;
                }
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * getSpeed() * delta,
                        getPosition().y + (float) Math.sin(direction) * getSpeed() * delta)
                );
                setOrientation(direction);
/*
                toAdd.add(new Exhaust("exhaust", new Vector2(this.getPosition().x - getCollisionRadius() * (float) Math.cos(this.getOrientation()),
                        this.getPosition().y - getCollisionRadius() * (float) Math.sin(this.getOrientation())), getSpace(),
                        -this.getOrientation(), getCollisionRadius() / 5));
                        */
                break;
            case BACKWARDS:
                setPosition(new Vector2(getPosition().x - (float) Math.cos(direction) * getSpeed() * delta,
                        getPosition().y - (float) Math.sin(direction) * getSpeed() * delta)
                );
                setOrientation((float) (direction + Math.PI));
                break;
            case DODGING:
                setPosition(new Vector2((getPosition().x + (float) Math.cos(direction + randomAngle(delta)) * getSpeed() * delta ),
                        (getPosition().y + (float) Math.sin(direction + randomAngle(delta)) * getSpeed() * delta))
                );

                break;
            case FLY_AROUND:
                setPosition(new Vector2((getPosition().x + (float) Math.cos(direction + updateAngle(delta)) * getSpeed() * delta ),
                        (getPosition().y + (float) Math.sin(direction + updateAngle(delta)) * getSpeed() * delta))
                );
                setOrientation(direction);

                break;

            case FLY_BY:
                time += delta;
                if(time >= getChangeDirectionTime()){
                    setSpeed(getSpeed() + 200f);
                    time = 0;
                }
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction + updateAngle(delta)) * getSpeed() * delta,
                        getPosition().y + (float) Math.sin(direction + updateAngle(delta)) * getSpeed() * delta)
                );
                setOrientation(direction);
                break;
            case FACE_HUGGING:
                rotationAngle += 3f * delta;
                setPosition(new Vector2((float) (getTargetVector().x + Math.cos(rotationAngle) * 45f),
                        (float) (getTargetVector().y + Math.sin(rotationAngle) * 45f)));

                setOrientation(direction);

                break;
            case FROZEN:

                break;
        }

        /*All types of attacks an enemy can do.
        * Every type spawns a enemyweaponry gameObject every "shotcount"
        * The type an individual enemy uses is handled in child classes*/

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
                            2f, BulletMechanics.determineBulletDamage()));
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
                            getSpace(), getOrientation(), 300,5f, MissileMechanics.determineMissileDamage()));
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
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), getDirection(), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /6), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /6), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /7), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /7), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /8), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /8), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() - Math.PI /9), 400, 2f, BulletMechanics.determineBulletDamage()));
                    toAdd.add(new BulletEnemy("bullito", getPosition(), getSpace(), (float) (getDirection() + Math.PI /9), 400, 2f, BulletMechanics.determineBulletDamage()));

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
                            getSpace(), getOrientation() + randomGenerator.nextFloat() * 100f, 200,2f, BulletMechanics.determineBulletDamage() / 5));
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
                    toAdd.add(new LaserBeamEnemy("laser", getPosition(), getSpace(), getOrientation(), 2f, BulletMechanics.determineBulletDamage()));
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
                    toAdd.add(new SpaceMineEnemy("enemy_mine", getPosition(), getSpace(), getOrientation(), 300, 8f, MineMechanics.determineMineDamage()));
                }

                break;
            case SELF_DESTRUCT:
                toDelete.add(this);
                initDebris(toDelete, toAdd);
                break;
 //TODO: fix blink
            case BLINK:
                if(time >= 3) {
                    setPosition(new Vector2(
                            getPosition().x + delta * getSpeed() * (random.nextFloat() * 200 - 100),
                            getPosition().y + delta * getSpeed() * (random.nextFloat() * 200 - 100)
                    ));
                    time = 0;
                }
                break;
            case GANG_VOILENCE:
                EnemyGang enemyGang = new EnemyGang("Gang!", new Vector2(
                        targetVector.x + getCollisionRadius() * 2, targetVector.y + getCollisionRadius() * 2),
                        50,random.nextFloat() * 2000 - 1000,120f,10f, getSpace());
                toAdd.add(enemyGang);
                break;
            case PACIFIST:
                shotLeftOver = 0;
                missilesShotLeftOver = 0;
                shottyShotLeftOver = 0;
                sporesShotLeftOver = 0;
                childrenShotLeftOver = 0;
                laserShotLeftOver = 0;
                minesLeft = 0;
                break;
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
    public float getHealth() {
        return health;
    }

    @Override
    public void setHealth(float health) {
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

    public AttackState getAttackState() {
        return attackState;
    }

    public void setAttackState(AttackState attackState) {
        this.attackState = attackState;
    }

    public MovingState getMovingState() {
        return movingState;
    }

    public void setMovingState(MovingState movingState) {
        this.movingState = movingState;
    }

    public Vector2 getTargetVector() {
        return targetVector;
    }

    public void setTargetVector(Vector2 targetVector) {
        this.targetVector = targetVector;
    }

    public float getGangAngle() {
        return gangAngle;
    }

    public void setGangAngle(float gangAngle) {
        this.gangAngle = gangAngle;
    }

    public float getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(float damageTaken) {
        this.damageTaken = damageTaken;
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
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("damageTaken", damageTaken);

        return result;
    }
}

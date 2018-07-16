package com.wisekrakr.firstgame.engine.gameobjects.enemies;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions.EnemyChaser;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.*;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;




public class Enemy extends GameObject {

    private Random random = new Random();

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

    private float rotationAngle;

    private float huggerRotationSpeed;
    private float mineAreaOfEffect;

    private float damageTaken = 0;
    private boolean hit = false;
    private float healthPercentage;
    private float maxHealth;
    private float updatedAngle;
    private float randomAngle;

    public Enemy(GameObjectVisualizationType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;
        maxHealth = this.health;

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;
        missileAmmoCount = (int) Double.POSITIVE_INFINITY;
        missilesShotLeftOver = missileAmmoCount;
        shottyAmmoCount = (int) Double.POSITIVE_INFINITY;
        shottyShotLeftOver = shottyAmmoCount;
        sporesAmmoCount = (int) Double.POSITIVE_INFINITY;
        sporesShotLeftOver = sporesAmmoCount;
        childrenAmmoCount = 10;
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
       this.setDirection(this.getDirection() + (float) Math.PI);
    }



    private float healthInPercentages(){
        if (isHit()) {
            float z = getHealth() - getDamageTaken();
            healthPercentage = z / maxHealth * 100;
            healthPercentage /= 100;
        }
        return healthPercentage;
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
        if (subject instanceof Bullet) {
            if (((Bullet) subject).isPlayerBullet()) {
                float angle = GameHelper.angleBetween(this, subject);
                setMovingState(MovingState.DEFAULT_FORWARDS);
                setOrientation(angle);
                setDirection(angle);
                setHit(true);
                setDamageTaken(subject.getDamage());
            }
        }
        if (subject instanceof HomingMissile){
            if(((HomingMissile) subject).isPlayerMissile()){
                float angle = GameHelper.angleBetween(this, subject);
                setMovingState(MovingState.DEFAULT_FORWARDS);
                setOrientation(angle);
                setDirection(angle);
                setHit(true);
                setDamageTaken(subject.getDamage());
                toDelete.add(subject);
            }
        }
        if (subject instanceof SpaceMine){
            if(((SpaceMine) subject).isPlayerMine()){
                float angle = GameHelper.angleBetween(this, subject);
                setMovingState(MovingState.DEFAULT_FORWARDS);
                setOrientation(angle);
                setDirection(angle);
                setHit(true);
                setDamageTaken(subject.getDamage());
            }
        }
    }

    /*This method counts as a way to prevent enemies from overlapping each other while attacking or flying towards one
    * another.*/

    @Override
    public void overlappingObjects(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Enemy){
            float angle = GameHelper.angleBetween(this, subject);
            if(GameHelper.distanceBetween(this, subject)<= getCollisionRadius() + subject.getCollisionRadius()) {
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
     */

    public enum AttackState {
        PACIFIST, BLINK, FIRE_BULLETS, FIRE_MISSILES, FIRE_MINES, FIRE_CHILDREN, FIRE_SPORES, FIRE_SHOTGUN,
        FIRE_LASER, SELF_DESTRUCT
    }

    public enum MovingState {
        FROZEN, DEFAULT_FORWARDS, BACKWARDS, DODGING, FLY_AROUND, FACE_HUGGING,
    }


    /*For most enemies this method counts as a way to move towards the player.
    * Some children classes have their own @Override of this method for a different action.
    * -If this enemy's health is more than 10% of its health it will get an Orientation and Direction towards the target
    * which is always the player
    * -Else it will no longer shoot and move away from the player.*/
//TODO: when enemy follows player out of bounds it will keep a safe distance and does not shoot, but will keep following
    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (GameHelper.distanceBetween(this, target) <= getAggroDistance() && getAggroDistance() >= getCollisionRadius() + target.getCollisionRadius()) {
                float angle = GameHelper.angleBetween(this, target);
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
        if (target != null) {
            if (target instanceof Player) {
                if (target.getHealth() <= 0) {
                    attackState = AttackState.PACIFIST;
                }
            }
        }else {
            attackState = AttackState.PACIFIST;
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

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
                    float randomDirection = EnemyMechanics.setRandomDirection();
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
                time += delta;

                if (time >= getChangeDirectionTime()) {
                    randomAngle = (float) (random.nextInt(180) * Math.PI * delta);
                    time = 0;
                }
                setPosition(new Vector2((getPosition().x +
                        (float) Math.cos(direction + randomAngle) * getSpeed() * delta ),
                        (getPosition().y +
                                (float) Math.sin(direction + randomAngle) * getSpeed() * delta))
                );
                setOrientation(direction);
                break;
            case FLY_AROUND:
                time += delta;

                if (time >= getChangeDirectionTime()) {
                    updatedAngle = (float) (45 * Math.PI * delta);
                    time = 0;
                }
                setPosition(new Vector2((getPosition().x +
                        (float) Math.cos(direction + updatedAngle) * getSpeed() * delta ),
                        (getPosition().y +
                                (float) Math.sin(direction + updatedAngle) * getSpeed() * delta))
                );
                setOrientation(direction);

                break;

            case FACE_HUGGING:
                rotationAngle += 3f * delta;
                setHuggerRotationSpeed(getSpeed()/2);
                if (getHealth() <= getHealth()*(10f/100f )){
                    setHuggerRotationSpeed(getSpeed());
                }
                setPosition(new Vector2((float) (getTargetVector().x + Math.cos(rotationAngle) * getHuggerRotationSpeed()),
                        (float) (getTargetVector().y + Math.sin(rotationAngle) * getHuggerRotationSpeed())));

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
                float shotCount = delta / WeaponMechanics.fireRate(5f) + shotLeftOver;
                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(EnemyWeaponry.loadBullet(this));
                }

                break;

            case FIRE_MISSILES:
                missileAmmoCount = getMissileAmmoCount();
                float missileShotCount = delta / WeaponMechanics.fireRate(20f) + missilesShotLeftOver;
                int missileExactShotCount = Math.min(Math.round(missileShotCount), missileAmmoCount);

                missileAmmoCount = missileAmmoCount - missileExactShotCount;
                if (missileAmmoCount > 0) {
                    missilesShotLeftOver = missileShotCount - missileExactShotCount;
                } else {
                    missilesShotLeftOver = 0;
                }

                for (int i = 0; i < missileExactShotCount; i++) {
                    toAdd.add(EnemyWeaponry.loadMissile(this));
                }

                break;
//TODO: This needs to be fixed. The way of making them, aiming them and the speed of them. THIS WHOLE THING NEEDS TO BE FIXED
            case FIRE_SHOTGUN:
                shottyAmmoCount = getShottyAmmoCount();
                float shottyShotCount = delta / WeaponMechanics.fireRate(40f) + shottyShotLeftOver;
                int exactShottyShotCount = Math.min(Math.round(shottyShotCount), shottyAmmoCount);

                shottyAmmoCount = shottyAmmoCount - exactShottyShotCount;
                if (shottyAmmoCount > 0) {
                    shottyShotLeftOver = shottyShotCount - exactShottyShotCount;
                } else {
                    shottyShotLeftOver = 0;
                }

                for (int i = 0; i < exactShottyShotCount; i++) {
                    Bullet bullet = new Bullet("bullito", getPosition(), getDirection(), getSpeed(),
                            BulletMechanics.radius(1), BulletMechanics.determineBulletDamage());
                    toAdd.add(bullet);
                    bullet.setEnemyBullet(true);
                    bullet.setBulletSpeed(getSpeed() * 10);

                }
                break;

            case FIRE_SPORES:
                sporesAmmoCount = getSporesAmmoCount();
                float sporesShotCount = delta / WeaponMechanics.fireRate(2f) + sporesShotLeftOver;

                int sporesExactShotCount = Math.min(Math.round(sporesShotCount), sporesAmmoCount);

                sporesAmmoCount = sporesAmmoCount - sporesExactShotCount;
                if (sporesAmmoCount > 0) {
                    sporesShotLeftOver = sporesShotCount - sporesExactShotCount;
                } else {
                    sporesShotLeftOver = 0;
                }

                for (int i = 0; i < sporesExactShotCount; i++) {
                    toAdd.add(EnemyWeaponry.loadSpores(this));
                }

                break;
//TODO: children should be any enemy you like
            case FIRE_CHILDREN:
                childrenAmmoCount = getChildrenAmmoCount();
                float childrenShotCount = delta / WeaponMechanics.fireRate(20f) + childrenShotLeftOver;

                int childrenExactShotCount = Math.min(Math.round(childrenShotCount), childrenAmmoCount);

                childrenAmmoCount = childrenAmmoCount - childrenExactShotCount;
                if (childrenAmmoCount > 0) {
                    childrenShotLeftOver = childrenShotCount - childrenExactShotCount;
                } else {
                    childrenShotLeftOver = 0;
                }
                for(int i = 0; i < childrenExactShotCount; i++) {
                    Random randomGenerator = new Random();
                    EnemyChaser enemyChaser = new EnemyChaser("ChaserMinion" + String.valueOf(childrenAmmoCount), new Vector2(
                            getPosition().x + randomGenerator.nextFloat() * radius,
                            getPosition().y + randomGenerator.nextFloat() * radius),
                            (int) (getMaxHealth()/10), getDirection(), getSpeed() * 6,
                            MinionMechanics.radius(1));
                    toAdd.add(enemyChaser);

                    float destructTime = 8.0f;
                    time += delta;
                    if(time >= destructTime){
                        float angle = GameHelper.angleBetween(this, enemyChaser);
                        enemyChaser.setPosition(new Vector2(this.getPosition().x +=  Math.cos(angle)  , this.getPosition().y +=  Math.sin(angle) ));
                        enemyChaser.setOrientation(angle);
                        enemyChaser.setDirection(angle);
                    }
                }
                break;

            case FIRE_LASER:
                laserAmmoCount = getLaserAmmoCount();
                float laserShotCount = delta / WeaponMechanics.fireRate(5f) + laserShotLeftOver;

                int laserExactShotCount = Math.min(Math.round(laserShotCount), laserAmmoCount);

                laserAmmoCount = laserAmmoCount - laserExactShotCount;
                if (laserAmmoCount > 0) {
                    laserShotLeftOver = laserShotCount - laserExactShotCount;
                } else {
                    laserShotLeftOver = 0;
                }

                for (int i = 0; i < laserExactShotCount; i++) {
                    toAdd.add(EnemyWeaponry.loadLaser(this));
                }

                break;

            case FIRE_MINES:
                minesCount = getMinesCount();
                float minesShotCount = delta / WeaponMechanics.fireRate(20f) + minesLeft;

                int exactMinesShotCount = Math.min(Math.round(minesShotCount), minesCount);

                minesCount = minesCount - exactMinesShotCount;
                if (minesCount > 0) {
                    minesLeft = minesShotCount - exactMinesShotCount;
                } else {
                    minesLeft = 0;
                }

                for (int i = 0; i < exactMinesShotCount; i++) {
                    toAdd.add(EnemyWeaponry.loadMine(this));
                }

                break;
            case SELF_DESTRUCT:
                toDelete.add(this);
                initDebris(toDelete, toAdd);
                break;
 //TODO: fix blink
            case BLINK:
                if(time >= 5) {
                    setPosition(new Vector2(
                            getPosition().x + delta * getSpeed() * (random.nextFloat() * 200 - 100),
                            getPosition().y + delta * getSpeed() * (random.nextFloat() * 200 - 100)
                    ));
                    time = 0;
                }
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

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
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

    public float getMaxHealth() {
        return maxHealth;
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


    public float getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(float damageTaken) {
        this.damageTaken = damageTaken;
    }

    public float getMineAreaOfEffect() {
        return mineAreaOfEffect;
    }

    public void setMineAreaOfEffect(float mineAreaOfEffect) {
        this.mineAreaOfEffect = mineAreaOfEffect;
    }

    public float getHuggerRotationSpeed() {
        return huggerRotationSpeed;
    }

    public void setHuggerRotationSpeed(float huggerRotationSpeed) {
        this.huggerRotationSpeed = huggerRotationSpeed;
    }

    public float getUpdatedAngle() {
        return updatedAngle;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);
        result.put("health", health);
        result.put("healthPercentage", healthInPercentages());
        result.put("damageTaken", damageTaken);
        result.put("hit", hit);
        result.put("maxHealth", maxHealth);

        return result;
    }
}

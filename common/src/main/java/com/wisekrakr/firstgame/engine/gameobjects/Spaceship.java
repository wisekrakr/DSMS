package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MineMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MinionMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.missions.QuestGen;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpHealth;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpMinion;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpMissile;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpShield;
import com.wisekrakr.firstgame.engine.gameobjects.spaceshipparts.Exhaust;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Spaceship extends GameObject {

    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    private SpecialPowerState powerState = SpecialPowerState.NO_POWER;
    private ShootingState shootingState = ShootingState.PACIFIST;
    private AimingState aimingState = AimingState.NONE;
    private PowerUpState powerUpState = PowerUpState.NONE;
    private SwitchWeaponState switchWeaponState = SwitchWeaponState.NONE;

    private Float hardSteering;
    private float speedX = 0;
    private float speedY = 0;
    private float angle = (float) (Math.PI / 2);
    private float distanceTravelled = 0;
    private int ammoCount;
    private int missileAmmoCount;
    private float shotLeftOver;
    private float missileLeftOver;
    private float health;
    private float score;

    private Minion minionShooterPlayer;
    private Minion minionFighterPlayer;
    private Shield shield;

    private float lastDodge = -100000f;
    private float time;
    private int mineAmmoCount;
    private float minesLeftOver;
    private int randomMinion;
    private float minionRotationSpeed;
    private float mineAreaOfEffect;
    private boolean minionShooterActivated = false;
    private boolean minionFighterActivated = false;
    private float speed;
    private float minionAngle;
    private float defaultSpeed = 60f;
    private float maxSpeed = 125f;

    private boolean isKilled;
    private String killerName;
    private boolean shieldActivated;

    private float damageTaken = 0;
    private boolean hit = false;
    private boolean pickedUp = false;
    private float healthPercentage;
    private float maxHealth;


    public Spaceship(String name, Vector2 position) {
        super(GameObjectVisualizationType.SPACESHIP, name, position);

        ammoCount = 10000;
        missileAmmoCount = 50;
        mineAmmoCount = 20;
        health = 750;
        score = 0;
        maxHealth = health;

        setCollisionRadius(5f);

    }


    public void modifySpeed(float v) {
        speedX = speedX * v;
        speedY = speedY * v;
    }

    public enum ThrottleState {
        REVERSE, STATUSQUO, FORWARDS, FULL_STOP
    }

    public enum SteeringState {
        LEFT, CENTER, RIGHT,
    }

    public enum SpecialPowerState {
        NO_POWER, BOOSTING, ULTRA_DODGE
    }

    public enum ShootingState {
        PACIFIST, FIRING, MISSILE_FIRING, PLACE_MINE
    }

    public enum AimingState {
        LEFT_BEAM_ANGLE, RIGHT_BEAM_ANGLE, NONE
    }

    public enum PowerUpState {
        MINION, NONE
    }

    public enum SwitchWeaponState {
        NONE("Peace 4 All, man"), BULLETS("Bullitos"), MISSILES("Homers"), SPACE_MINES("Blinky Boom");

        private String fieldDescription;

        SwitchWeaponState(String normalText) {
            fieldDescription = normalText;
        }

        public String getFieldDescription() {
            return fieldDescription;
        }
    }


    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {

        // angle = angle + (float) Math.PI;
    }

    private String killedBy() {
        if (isKilled) {
            return killerName;
        }
        return null;
    }

    private float healthInPercentages() {
        if (isHit()) {
            float z = getHealth() - getDamageTaken();
            healthPercentage = z / maxHealth * 100;
            healthPercentage /= 100;
        }
        return healthPercentage;
    }

    private float fireRate(float rateOfFire) {
        return rateOfFire;
    }

    public void control(ThrottleState throttle, SteeringState steering, SpecialPowerState powerState, ShootingState shootingState,
                        AimingState aimingState, SwitchWeaponState switchWeaponState, Float hardSteering) {
        this.throttle = throttle;
        this.steering = steering;
        this.powerState = powerState;
        this.shootingState = shootingState;
        this.aimingState = aimingState;
        this.switchWeaponState = switchWeaponState;
        this.hardSteering = hardSteering;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Enemy) {
            if (isKilled) {
                setKillerName(subject.getName());
            }
        }


        if (subject instanceof Bullet) {
            if (((Bullet) subject).isEnemyBullet()) {
                setHit(true);
                setDamageTaken(subject.getDamage());
                if (isKilled) {
                    setKillerName(subject.getName());
                }
            } else {
                setHit(false);
            }
        }
        if (subject instanceof HomingMissile) {
            if (((HomingMissile) subject).isEnemyMissile()) {
                setHit(true);
                setDamageTaken(subject.getDamage());
                setKillerName(subject.getName());
            } else {
                setHit(false);
            }
        }
        if (subject instanceof SpaceMine) {
            if (((SpaceMine) subject).isEnemyMine()) {
                setHit(true);
                setDamageTaken(subject.getDamage());
                setKillerName(subject.getName());
            } else {
                setHit(false);
            }
        }


        if (subject instanceof PowerUpShield) {
            toDelete.add(subject);
            shield = new Shield("shield", getPosition(), getAngle(), getSpeed(), this.getCollisionRadius() * 2, MissileMechanics.determineMissileDamage());
            toAdd.add(shield);
            shieldActivated = true;
        }
        if (subject instanceof PowerUpMissile) {
            toDelete.add(subject);
            this.setMissileAmmoCount(this.getMissileAmmoCount() + 20);
        }
        if (subject instanceof PowerUpHealth) {
            toDelete.add(subject);
            this.setHealth(this.getHealth() + 100);
        }
        if (subject instanceof PowerUpMinion) {
            toDelete.add(subject);
            randomMinion = MathUtils.random(1, 2);
            switch (randomMinion) {
                case 1:
                    InitMinionShooter(toDelete, toAdd);
                    break;
                case 2:
                    InitMinionFighter(toDelete, toAdd);
                    break;

            }
        }

        if (subject instanceof QuestGen) {
            pickedUp = true;
            toDelete.add(subject);
        }
    }

    private boolean collisionDetected(GameObject object1, GameObject object2){
        return
                Math.sqrt(
                        (((object1.getPosition().x) - (object2.getPosition().x)))
                                * ((object1.getPosition().x) - (object2.getPosition().x))
                                + ((object1.getPosition().y) - (object2.getPosition().y))
                                * ((object1.getPosition().y) - (object2.getPosition().y)))
                        < (object1.getCollisionRadius() + object2.getCollisionRadius());

    }

    public void scoringSystem(GameObject enemy, GameObject subject) {

        if (enemy instanceof Enemy) {
            if (subject instanceof Bullet || subject instanceof HomingMissile || subject instanceof SpaceMine) {
                if (collisionDetected(enemy, subject)) {
                    if (subject instanceof Bullet) {
                        this.setScore(this.getScore() + (subject).getDamage());
                        if (enemy.getHealth() <= 0) {
                            this.setScore(this.getScore() + 50);
                        }
                    }
                    if (subject instanceof HomingMissile) {
                        this.setScore(this.getScore() + (subject).getDamage());
                        if (enemy.getHealth() <= 0) {
                            this.setScore(this.getScore() + 100);
                        }
                    }
                    if (subject instanceof SpaceMine) {
                        this.setScore(this.getScore() + (subject).getDamage());
                        if (enemy.getHealth() <= 0) {
                            this.setScore(this.getScore() + 200);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        //Player gets deleted when health is 0
        if (health <= 0) {
            toDelete.add(this);
            isKilled = true;
        }

        if (hardSteering != null) {
            //angle = (float) Math.atan2(Gdx.input.getY()- getPosition().y, Gdx.input.getX()- getPosition().x);
            //angle = angle + hardSteering * delta;
            angle = hardSteering;
            hardSteering = null;
        } else {
            switch (steering) {
                case LEFT:
                    angle = angle + 3f * delta;
                    break;
                case RIGHT:
                    angle = angle - 3f * delta;
                    break;
            }
        }
        setOrientation(angle);

        switch (throttle) {
            case FORWARDS:
                speedX = speedX + delta * defaultSpeed * (float) Math.cos(angle);
                speedY = speedY + delta * defaultSpeed * (float) Math.sin(angle);
                toAdd.add(new Exhaust("exhaust", this.getPosition(), -this.getOrientation(), getCollisionRadius() / 5));
                break;

            case REVERSE:
                speedX = speedX - delta * defaultSpeed * (float) Math.cos(angle);
                speedY = speedY - delta * defaultSpeed * (float) Math.sin(angle);
                break;

            case STATUSQUO:
                /*
                setOrientation(angle); // this way i can choose an angle and shoot that way
                setPosition(new Vector2(
                        getPosition().x + delta * speed * angle,
                        getPosition().y + delta * speed * angle
                ));
                */

                break;
            case FULL_STOP:
                speedX = 0;
                speedY = 0;
                break;

        }

        speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);

        if (speed > (defaultSpeed + (defaultSpeed / 2))) {
            speedX = speedX * (defaultSpeed + (defaultSpeed / 2)) / speed;
            speedY = speedY * (defaultSpeed + (defaultSpeed / 2)) / speed;
        }


        switch (powerState) {
            case BOOSTING:
                speedX = speedX + (float) Math.cos(angle) * Math.min(speed + (defaultSpeed + (defaultSpeed / 2)), maxSpeed);
                speedY = speedY + (float) Math.sin(angle) * Math.min(speed + (defaultSpeed + (defaultSpeed / 2)), maxSpeed);

                speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
                if (speed > maxSpeed) {
                    speedX = speedX * maxSpeed / speed;
                    speedY = speedY * maxSpeed / speed;
                }

                toAdd.add(new Exhaust("exhaust", getPosition(), -this.getOrientation(), getCollisionRadius() / 3));
                break;

            case ULTRA_DODGE:
                if (clock - lastDodge > 10) {
                    lastDodge = clock;
                    Random random = new Random();
                    setPosition(new Vector2(
                            getPosition().x + delta * speed * (getOrientation() + random.nextFloat() * 200 - 100),
                            getPosition().y + delta * speed * (getOrientation() + random.nextFloat() * 200 - 100)
                    ));

                }

                break;
        }

        //Player movement
        setPosition(new Vector2(
                getPosition().x + delta * speedX,
                getPosition().y + delta * speedY
        ));

        //if shieldActivated set shield to player position
        if (shieldActivated) {
            shield.setPosition(getPosition());
        }

        distanceTravelled = distanceTravelled + Math.abs(delta * speed);


        switch (shootingState) {
            case FIRING:
                activateBullets(delta, toDelete, toAdd);
                break;
            case MISSILE_FIRING:
                activateMissiles(delta, toDelete, toAdd);
                break;
            case PLACE_MINE:
                activateSpaceMines(delta, toDelete, toAdd);
                break;
            case PACIFIST:
                shotLeftOver = 0;
                missileLeftOver = 0;
                minesLeftOver = 0;
                break;
        }
        setMinionRotationSpeed(getSpeed() / 100);
        switch (powerUpState) {
            case MINION:
                if (randomMinion == 1) {
                    if (minionShooterActivated) {
                        minionAngle += getMinionRotationSpeed() * delta;
                        minionShooterPlayer.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 80f),
                                (float) (getPosition().y + Math.sin(minionAngle) * 80f)));
                    }
                }
                if (randomMinion == 2) {
                    if (minionFighterActivated) {
                        if (minionFighterPlayer.getMinionAttackState() != Minion.MinionAttackState.FIGHT) {
                            minionAngle += getMinionRotationSpeed() * delta;
                            minionFighterPlayer.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 80f),
                                    (float) (getPosition().y + Math.sin(minionAngle) * 80f)));
                        }
                    }
                }
                break;
            case NONE:
                break;

        }
    }

    private void activateBullets(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float shotCount = delta / fireRate(0.2f) + shotLeftOver;

        int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

        ammoCount = ammoCount - exactShotCount;
        if (ammoCount > 0) {
            shotLeftOver = shotCount - exactShotCount;
        } else {
            shotLeftOver = 0;
        }

        for (int i = 0; i < exactShotCount; i++) {

            Bullet currentBullet = new Bullet("bullito", getPosition(), getAngle(), getSpeed(),
                    BulletMechanics.radius(1), BulletMechanics.determineBulletDamage());
            toAdd.add(currentBullet);
            currentBullet.setPlayerBullet(true);
            currentBullet.setBulletSpeed(defaultSpeed * 3);
        }

    }

    private void activateMissiles(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float missileCount = delta / fireRate(0.5f) + missileLeftOver;

        int exactMissileCount = Math.min(Math.round(missileCount), missileAmmoCount);

        missileAmmoCount = missileAmmoCount - exactMissileCount;
        if (missileAmmoCount > 0) {
            missileLeftOver = missileCount - exactMissileCount;
        } else {
            missileLeftOver = 0;
        }
        for (int i = 0; i < exactMissileCount; i++) {
            HomingMissile currentMissile = new HomingMissile("missilito", getPosition(), getAngle(), getSpeed(),
                    MissileMechanics.radius(1), MissileMechanics.determineMissileDamage(), true);
            toAdd.add(currentMissile);
            currentMissile.missileEnable(this);
            currentMissile.setMissileSpeed(defaultSpeed * 2);
        }

    }

    private void activateSpaceMines(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float minesCount = delta / fireRate(1f) + minesLeftOver;

        int exactMineCount = Math.min(Math.round(minesCount), mineAmmoCount);

        mineAmmoCount = mineAmmoCount - exactMineCount;
        if (mineAmmoCount > 0) {
            minesLeftOver = minesCount - exactMineCount;
        } else {
            minesLeftOver = 0;
        }

        for (int i = 0; i < exactMineCount; i++) {
            setMineAreaOfEffect(10f);
            SpaceMine currentSpaceMine = new SpaceMine("mine", getPosition(), getAngle(), getSpeed(),
                    MineMechanics.radius(1), getMineAreaOfEffect(), MineMechanics.determineMineDamage());
            toAdd.add(currentSpaceMine);
            currentSpaceMine.setPlayerMine(true);

        }
    }

    private Minion InitMinionShooter(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        minionShooterPlayer = new Minion("minion_shooter", new Vector2(
                getPosition().x + (getCollisionRadius() * 2),
                getPosition().y + (getCollisionRadius() * 2)), MinionMechanics.determineHealth(), getAngle(),
                MinionMechanics.radius(1));
        toAdd.add(minionShooterPlayer);
        powerUpState = PowerUpState.MINION;
        minionShooterActivated = true;
        minionShooterPlayer.setMinionShooter(true);
        minionShooterPlayer.setPlayerMinion(true);

        return minionShooterPlayer;
    }

    private Minion InitMinionFighter(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        minionFighterPlayer = new Minion("minion_fighter", new Vector2(
                getPosition().x + (getCollisionRadius() * 2),
                getPosition().y + (getCollisionRadius() * 2)),
                MinionMechanics.determineHealth(),
                getAngle(), MinionMechanics.radius(1));
        toAdd.add(minionFighterPlayer);
        powerUpState = PowerUpState.MINION;
        minionFighterActivated = true;
        minionFighterPlayer.setMinionFighter(true);
        minionFighterPlayer.setPlayerMinion(true);
        minionFighterPlayer.setTargetVector(this.getPosition());
        return minionFighterPlayer;
    }

    @Override
    public float getHealth() {
        return health;
    }

    @Override
    public void setHealth(float health) {
        this.health = health;
    }

    public int getMissileAmmoCount() {
        return missileAmmoCount;
    }

    public void setMissileAmmoCount(int missileAmmoCount) {
        this.missileAmmoCount = missileAmmoCount;
    }

    public ShootingState getShootingState() {
        return shootingState;
    }

    public void setShootingState(ShootingState shootingState) {
        this.shootingState = shootingState;
    }

    public float getAngle() {
        return angle;
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public float getDistanceTravelled() {
        return distanceTravelled;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }


    public int getMineAmmoCount() {
        return mineAmmoCount;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public void setKillerName(String killerName) {
        this.killerName = killerName;
    }

    public float getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(float damageTaken) {
        this.damageTaken = damageTaken;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getMineAreaOfEffect() {
        return mineAreaOfEffect;
    }

    public void setMineAreaOfEffect(float mineAreaOfEffect) {
        this.mineAreaOfEffect = mineAreaOfEffect;
    }

    public float getMinionRotationSpeed() {
        return minionRotationSpeed;
    }

    public void setMinionRotationSpeed(float minionRotationSpeed) {
        this.minionRotationSpeed = minionRotationSpeed;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("distanceTravelled", distanceTravelled);

        if (switchWeaponState == SwitchWeaponState.BULLETS) {
            result.put("ammoCount", getAmmoCount());
        } else if (switchWeaponState == SwitchWeaponState.MISSILES) {
            result.put("ammoCount", getMissileAmmoCount());
        } else if (switchWeaponState == SwitchWeaponState.SPACE_MINES) {
            result.put("ammoCount", getMineAmmoCount());
        } else {
            result.put("ammoCount", 0);
        }
        result.put("health", health);

        result.put("score", score);
        result.put("switchWeaponState", switchWeaponState.getFieldDescription());

        result.put("killedBy", killedBy());

        result.put("healthPercentage", healthInPercentages());

        result.put("damageTaken", damageTaken);

        result.put("maxHealth", maxHealth);

        result.put("hit", hit);

        result.put("pickedUp", pickedUp);

        result.put("speed", speed);

        return result;
    }
}

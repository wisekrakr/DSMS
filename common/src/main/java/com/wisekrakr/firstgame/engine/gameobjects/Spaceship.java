package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MineMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.*;
import com.wisekrakr.firstgame.engine.gameobjects.spaceshipparts.Exhaust;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.*;

import java.util.*;

public class Spaceship extends GameObject {

    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    private SpecialPowerState powerState = SpecialPowerState.NO_POWER;
    private ShootingState shootingState = ShootingState.PACIFIST;
    private AimingState aimingState = AimingState.NONE;
    private PowerUpState powerUpState = PowerUpState.NONE;
    private SwitchWeaponState switchWeaponState = SwitchWeaponState.NONE;

    private static final float FIRE_RATE = 0.5f;
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

    private List<Bullet> bullets;
    private List<HomingMissile> missiles;
    private Bullet currentBullet;
    private HomingMissile currentMissile;
    private List<SpaceMinePlayer> spaceMines;
    private SpaceMinePlayer currentSpaceMine;
    private MinionShooterPlayer minionShooterPlayer;
    private MinionFighterPlayer minionFighterPlayer;
    private Shield shield;

    private float lastDodge = -100000f;
    private float time;
    private int mineAmmoCount;
    private float minesLeftOver;
    private int randomMinion;
    private boolean minionShooterActivated = false;
    private boolean minionFighterActivated = false;
    private float speed;
    private float minionAngle;
    private float defaultSpeed = 240;
    private float maxSpeed = 500;

    private boolean isKilled;
    private String killerName;
    private boolean shieldActivated;
    private float lastShot;

    /*
    private float oldMouseX;
    private float oldMouseY;
    private float mouseX;
    private float mouseY;
    private float mouseAngle;
    */
    public Spaceship(String name, Vector2 position, SpaceEngine space) {
        super(GameObjectType.SPACESHIP, name, position, space);

        ammoCount = 10000;
        missileAmmoCount = 50;
        mineAmmoCount = 20;
        health = 1000;
        score = 0;

        setCollisionRadius(20f);

        bullets = new ArrayList<>();
        missiles = new ArrayList<>();
        spaceMines = new ArrayList<>();



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
        NONE, BULLETS, MISSILES, SPACE_MINES
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {

        // angle = angle + (float) Math.PI;
    }

    private String killedBy(){
        if (isKilled){
            return killerName;
        }
        return null;
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
/*
        if (subject instanceof Enemy) {
            subject.setHealth(subject.getHealth() - 20);
            setHealth(getHealth() - 20);
        }
        */
        if (subject instanceof PowerUpShield) {
            toDelete.add(subject);
            shield = new Shield("shield", getPosition(), getSpace(), getAngle(), getSpeed(), this.getCollisionRadius() * 2, MissileMechanics.determineMissileDamage());
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
                    minionShooterPlayer = new MinionShooterPlayer("minion_shooter", new Vector2(
                            getPosition().x + (getCollisionRadius() * 2),
                            getPosition().y + (getCollisionRadius() * 2)),
                            50,
                            getAngle(), 10, getSpace());
                    toAdd.add(minionShooterPlayer);
                    powerUpState = PowerUpState.MINION;
                    minionShooterActivated = true;
                    break;
                case 2:
                    minionFighterPlayer = new MinionFighterPlayer("minion_fighter", new Vector2(
                            getPosition().x + (getCollisionRadius() * 2),
                            getPosition().y + (getCollisionRadius() * 2)),
                            50,
                            getAngle(), 10, getSpace());
                    toAdd.add(minionFighterPlayer);
                    powerUpState = PowerUpState.MINION;
                    minionFighterActivated = true;
                    break;

            }

        }
    }

    public void scoringSystem(GameObject enemy, GameObject subject) {

        if (enemy instanceof Enemy){
            if (subject instanceof Bullet || subject instanceof HomingMissile || subject instanceof SpaceMinePlayer){
                if (isHit(enemy, subject)){
                    if (subject instanceof Bullet) {
                        this.setScore(this.getScore() + (subject).getDamage());
                        if (enemy.getHealth() <= 0) {
                            this.setScore(this.getScore() + 50);
                        }
                    }
                    if (subject instanceof HomingMissile){
                        this.setScore(this.getScore() + (subject).getDamage());
                        if (enemy.getHealth() <= 0) {
                            this.setScore(this.getScore() + 100);
                        }
                    }
                    if (subject instanceof SpaceMinePlayer){
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

/*        oldMouseX = mouseX;
        oldMouseY = mouseY;

        mouseX = Gdx.input.getX();
        mouseY = Gdx.input.getY();

        if((mouseX - oldMouseX) > 0){
            angle = angle + 6f * delta;
        }else if ((mouseX - oldMouseX) < 0){
            angle = angle - 6f * delta;
        }
*/
        //Player gets deleted when health is 0
        if (health <= 0) {
            toDelete.add(this);
            isKilled = true;
        }

        if (hardSteering != null) {
            angle = angle + hardSteering;
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

                toAdd.add(new Exhaust("exhaust", this.getPosition(), getSpace(), -this.getOrientation(), getCollisionRadius() / 5));
                break;

            case REVERSE:
                speedX = speedX - delta * defaultSpeed * (float) Math.cos(angle);
                speedY = speedY - delta * defaultSpeed * (float) Math.sin(angle);

//                speed = Math.max(speed - delta * 155f, -230);
                break;

            case STATUSQUO:
                /*
                setOrientation(angle); // this way i can choose a angle and shoot of that way
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

        if (speed > (defaultSpeed + (defaultSpeed/2))) {
            speedX = speedX * (defaultSpeed + (defaultSpeed/2)) / speed;
            speedY = speedY * (defaultSpeed + (defaultSpeed/2)) / speed;
        }


        switch (powerState) {
            case BOOSTING:
                speedX = speedX + (float) Math.cos(angle) * Math.min(speed + (defaultSpeed + (defaultSpeed/2)), maxSpeed);
                speedY = speedY + (float) Math.sin(angle) * Math.min(speed + (defaultSpeed + (defaultSpeed/2)), maxSpeed);

                speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
                if (speed > maxSpeed) {
                    speedX = speedX * maxSpeed / speed;
                    speedY = speedY * maxSpeed / speed;
                }

                toAdd.add(new Exhaust("exhaust", getPosition(), getSpace(), -this.getOrientation(), getCollisionRadius() / 3));
                break;

            case ULTRA_DODGE:
                if (clock - lastDodge > 10) {
                    lastDodge = clock;
                    Random random = new Random();
                    setPosition(new Vector2(
                            getPosition().x + delta * speed * (random.nextFloat() * 200 - 100),
                            getPosition().y + delta * speed * (random.nextFloat() * 200 - 100)
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
        if (shieldActivated){
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

        switch (powerUpState) {
            case MINION:
                if (randomMinion == 1) {
                    if (minionShooterActivated){
                        minionAngle += 2f * delta;
                        minionShooterPlayer.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 45f),
                                (float) (getPosition().y + Math.sin(minionAngle) * 45f)));
                    }
                }
                if (randomMinion == 2) {
                    if (minionFighterActivated){
                        if (minionFighterPlayer.getMinionAttackState() != Minion.MinionAttackState.FIGHT) {
                            minionAngle += 2f * delta;
                            minionFighterPlayer.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 45f),
                                    (float) (getPosition().y + Math.sin(minionAngle) * 45f)));
                        }
                    }
                }
                break;
            case NONE:
                break;

        }
    }

    private void activateBullets(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float shotCount = delta / 0.2f + shotLeftOver;

        int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

        ammoCount = ammoCount - exactShotCount;
        if (ammoCount > 0) {
            shotLeftOver = shotCount - exactShotCount;
        } else {
            shotLeftOver = 0;
        }

        for (int i = 0; i < exactShotCount; i++) {

            currentBullet = new Bullet("bullito", getPosition(), getSpace(), getAngle(), getSpeed(), 2f, BulletMechanics.determineBulletDamage());
            toAdd.add(currentBullet);
            currentBullet.setBulletState(Bullet.BulletState.ATTACK_ENEMY);

        }

    }

    private void activateMissiles(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float missileCount = delta / 0.5f + missileLeftOver;

        int exactMissileCount = Math.min(Math.round(missileCount), missileAmmoCount);

        missileAmmoCount = missileAmmoCount - exactMissileCount;
        if (missileAmmoCount > 0) {
            missileLeftOver = missileCount - exactMissileCount;
        } else {
            missileLeftOver = 0;
        }
        for (int i = 0; i < exactMissileCount; i++) {
            currentMissile = new HomingMissile("missilito", getPosition(), getSpace(), getAngle(), getSpeed(), 5f,
                    MissileMechanics.determineMissileDamage(), true);
            toAdd.add(currentMissile);
            currentMissile.missileEnable(this);
            currentMissile.setMissileSpeed(defaultSpeed * 2);
        }

    }

    private void activateSpaceMines(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float minesCount = delta / 2.0f + minesLeftOver;

        int exactMineCount = Math.min(Math.round(minesCount), mineAmmoCount);

        mineAmmoCount = mineAmmoCount - exactMineCount;
        if (mineAmmoCount > 0) {
            minesLeftOver = minesCount - exactMineCount;
        } else {
            minesLeftOver = 0;
        }

        for (int i = 0; i < exactMineCount; i++) {
            currentSpaceMine = new SpaceMinePlayer("mine", getPosition(), getSpace(), getAngle(), 300, 8f, MineMechanics.determineMineDamage());
            toAdd.add(currentSpaceMine);
        }
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

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("distanceTravelled", distanceTravelled);

        return result;
    }

    @Override
    public Map<String, Object> getAmmoProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        if (switchWeaponState == SwitchWeaponState.BULLETS) {
            result.put("ammoCount", getAmmoCount());
        } else if (switchWeaponState == SwitchWeaponState.MISSILES) {
            result.put("ammoCount", getMissileAmmoCount());
        } else if (switchWeaponState == SwitchWeaponState.SPACE_MINES) {
            result.put("ammoCount", getMineAmmoCount());
        } else {
            result.put("ammoCount", 0);
        }

        return result;
    }

    @Override
    public Map<String, Object> getHealthProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("health", health);

        return result;
    }

    @Override
    public Map<String, Object> getScoreProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("score", score);

        return result;
    }

    @Override
    public Map<String, Object> getRandomProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("switchWeaponState", switchWeaponState);

        return result;
    }

    @Override
    public Map<String, Object> getKilledByProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("isKilled", isKilled);
        result.put("killedBy", killedBy());

        return result;
    }
}

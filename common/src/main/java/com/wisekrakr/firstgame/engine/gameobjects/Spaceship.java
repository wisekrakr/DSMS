package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import com.wisekrakr.firstgame.engine.SpaceEngine;

import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.*;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Rotunda;
import com.wisekrakr.firstgame.engine.gameobjects.spaceshipparts.Exhaust;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.*;

import java.util.*;

public class Spaceship extends GameObject {


    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    public SpecialPowerState powerState = SpecialPowerState.NO_POWER;
    private ShootingState shootingState = ShootingState.PACIFIST;
    private AimingState aimingState = AimingState.NONE;
    private PowerUpState powerUpState = PowerUpState.NONE;
    private SwitchWeaponState switchWeaponState = SwitchWeaponState.NONE;

    private float speedX = 0;
    private float speedY = 0;
    private float angle = (float) (Math.PI / 2);
    private float distanceTravelled = 0;
    private int ammoCount;
    private int missileAmmoCount;
    private float shotLeftOver;
    private float missileLeftOver;
    private int health;
    private int score;

    private List<BulletPlayer> bullets;
    private List<MissilePlayer> missiles;
    private BulletPlayer currentBullet;
    private MissilePlayer currentMissile;
    private List<SpaceMinePlayer> spaceMines;
    private SpaceMinePlayer currentSpaceMine;
    private MinionShooterPlayer minionShooterPlayer;
    private MinionFighterPlayer minionFighterPlayer;
    private Rotunda rotunda;

    private float lastDodge = -100000f;
    private float time;
    private int mineAmmoCount;
    private float minesLeftOver;
    private int randomMinion;
    private boolean minionActivated = false;
    private float speed;

    private float oldMouseX;
    private float oldMouseY;
    private float mouseX;
    private float mouseY;

    public Spaceship(String name, Vector2 position, SpaceEngine space) {
        super(name, position, space);

        ammoCount = 10000;
        missileAmmoCount = 50;
        mineAmmoCount = 20;
        health = 1000;
        score = 0;

        setCollisionRadius(20f);

        bullets = new ArrayList<>();
        missiles = new ArrayList<>();
        spaceMines = new ArrayList<>();

        rotunda = new Rotunda("rotunda", new Vector2(
                getPosition().x + (getCollisionRadius() * 2) ,
                getPosition().y + (getCollisionRadius() * 2)),
                getSpace(), 10,
                getOrientation());

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
        SHIELD, MINION, NONE
    }
    public enum SwitchWeaponState {
        NONE, BULLETS, MISSILES, SPACE_MINES
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {

        // angle = angle + (float) Math.PI;
    }

    public void control(ThrottleState throttle, SteeringState steering, SpecialPowerState powerState, ShootingState shootingState,
                        AimingState aimingState, SwitchWeaponState switchWeaponState) {
        this.throttle = throttle;
        this.steering = steering;
        this.powerState = powerState;
        this.shootingState = shootingState;
        this.aimingState = aimingState;
        this.switchWeaponState = switchWeaponState;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Enemy) {
            subject.setHealth(subject.getHealth() - 20);
            setHealth(getHealth() - 20);
            /*
            Random random = new Random();
            int debrisParts = random.nextInt(10) + 1;
            for (int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", subject.getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 30, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * ((Enemy) subject).getRadius()));

            }
            */
        }
        if (subject instanceof PowerUpShield) {
            toDelete.add(subject);
            toAdd.add(new Shield("shield", getPosition(), getSpace(), getAngle(), getSpeed(), this.getCollisionRadius() * 2, randomDamageCountMissile()));
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
                            getPosition().x + (getCollisionRadius() * 2) ,
                            getPosition().y + (getCollisionRadius() * 2)),
                            50,
                            getAngle() , 10, getSpace());
                    toAdd.add(minionShooterPlayer);
                    powerUpState = PowerUpState.MINION;
                    minionActivated = true;
                    break;
                case 2:
                    minionFighterPlayer = new MinionFighterPlayer("minion_fighter", new Vector2(
                            getPosition().x + (getCollisionRadius() * 2),
                            getPosition().y + (getCollisionRadius() * 2)),
                            50,
                            getAngle(), 10, getSpace());
                    toAdd.add(minionFighterPlayer);
                    powerUpState = PowerUpState.MINION;
                    minionActivated = true;
                    break;

            }
        }
    }

    public void scoringSystem(GameObject enemy, GameObject subject) {

        if (enemy instanceof Enemy) {
            if (subject instanceof BulletPlayer) {
                if (Math.sqrt(
                        (((enemy.getPosition().x) - (subject.getPosition().x)))
                                * ((enemy.getPosition().x) - (subject.getPosition().x))
                                + ((enemy.getPosition().y) - (subject.getPosition().y))
                                * ((enemy.getPosition().y) - (subject.getPosition().y)))
                        < (enemy.getCollisionRadius() + subject.getCollisionRadius())) {
                    this.setScore(this.getScore() + ((BulletPlayer) subject).getDamage());
                    if (enemy.getHealth() <= 0) {
                        this.setScore(this.getScore() + 50);
                    }
                }
            }
        }

        if (enemy instanceof Enemy) {
            if (subject instanceof MissilePlayer) {
                if (Math.sqrt(
                        (((enemy.getPosition().x) - (subject.getPosition().x)))
                                * ((enemy.getPosition().x) - (subject.getPosition().x))
                                + ((enemy.getPosition().y) - (subject.getPosition().y))
                                * ((enemy.getPosition().y) - (subject.getPosition().y)))
                        < (enemy.getCollisionRadius() + subject.getCollisionRadius())) {
                    this.setScore(this.getScore() + ((MissilePlayer) subject).getDamage());
                    if (enemy.getHealth() <= 0) {
                        this.setScore(this.getScore() + 100);
                    }
                }
            }
        }

        if (enemy instanceof Enemy) {
            if (subject instanceof SpaceMinePlayer) {
                if (Math.sqrt(
                        (((enemy.getPosition().x) - (subject.getPosition().x)))
                                * ((enemy.getPosition().x) - (subject.getPosition().x))
                                + ((enemy.getPosition().y) - (subject.getPosition().y))
                                * ((enemy.getPosition().y) - (subject.getPosition().y)))
                        < (enemy.getCollisionRadius() + subject.getCollisionRadius())) {
                    this.setScore(this.getScore() + ((SpaceMinePlayer) subject).getDamage());
                    if (enemy.getHealth() <= 0) {
                        this.setScore(this.getScore() + 250);
                    }
                }
            }
        }

        if (enemy instanceof Enemy) {
            if (subject instanceof Shield) {
                if (Math.sqrt(
                        (((enemy.getPosition().x) - (subject.getPosition().x)))
                                * ((enemy.getPosition().x) - (subject.getPosition().x))
                                + ((enemy.getPosition().y) - (subject.getPosition().y))
                                * ((enemy.getPosition().y) - (subject.getPosition().y)))
                        < (enemy.getCollisionRadius() + subject.getCollisionRadius())) {
                    if (enemy.getHealth() <= 0) {
                        this.setScore(this.getScore() + 250);
                    }
                }
            }
        }

        if (enemy instanceof Enemy) {
            if (subject instanceof BulletMisc) {
                if (Math.sqrt(
                        (((enemy.getPosition().x) - (subject.getPosition().x)))
                                * ((enemy.getPosition().x) - (subject.getPosition().x))
                                + ((enemy.getPosition().y) - (subject.getPosition().y))
                                * ((enemy.getPosition().y) - (subject.getPosition().y)))
                        < (enemy.getCollisionRadius() + subject.getCollisionRadius())) {
                    if (enemy.getHealth() <= 0) {
                        this.setScore(this.getScore() + 100);
                    }
                }
            }
        }
    }


    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
/*
        oldMouseX = mouseX;
        oldMouseY = mouseY;

        mouseX = Gdx.input.getX();
        mouseY = Gdx.input.getY();

        if((mouseX - oldMouseX) > 0){
            angle = angle + 6f * delta;
        }else if ((mouseX - oldMouseX) < 0){
            angle = angle - 6f * delta;
        }
*/

        switch (steering) {
            case LEFT:
                angle = angle + 3f * delta;
                break;
            case RIGHT:
                angle = angle - 3f * delta;
                break;
        }
        setOrientation(angle);

        switch (throttle) {
            case FORWARDS:
                speedX = speedX + delta * 240f * (float) Math.cos(angle);
                speedY = speedY + delta * 240f * (float) Math.sin(angle);

                toAdd.add(new Exhaust("exhaust", this.getPosition(), getSpace(), -this.getOrientation(), getCollisionRadius() / 5));
                break;

            case REVERSE:
                speedX = speedX - delta * 240f * (float) Math.cos(angle);
                speedY = speedY - delta * 240f * (float) Math.sin(angle);

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

        if (speed > 320) {
            speedX = speedX * 320 / speed;
            speedY = speedY * 320 / speed;
        }


        switch (powerState) {
            case BOOSTING:
                speedX = speedX + (float) Math.cos(angle) * Math.min(speed + 320, 500);
                speedY = speedY + (float) Math.sin(angle) * Math.min(speed + 320, 500);

                speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
                if (speed > 500) {
                    speedX = speedX * 500 / speed;
                    speedY = speedY * 500 / speed;
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

        //Player gets deleted when health is 0
        if (health <= 0) {
            toDelete.add(this);
        }

        //Player movement
        setPosition(new Vector2(
                getPosition().x + delta * speedX,
                getPosition().y + delta * speedY
        ));


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
/*
                    minionShooterPlayer.setPosition(new Vector2((float) (getPosition().x + Math.PI * 3 * 300 * delta),
                            (float) (getPosition().y + Math.PI * 3 * 300 * delta))
                    );
*/
                    minionShooterPlayer.setPosition(new Vector2(getPosition().x + getCollisionRadius() * 2 + (float)Math.cos(Math.PI * 3) * getSpeed() * delta,
                            getPosition().y + getCollisionRadius() * 2 + (float)Math.sin(Math.PI * 3) * getSpeed() * delta));

                }

                if (randomMinion == 2) {
                    if (minionFighterPlayer.getMinionState() != Minion.MinionState.SHOOT) {
                        minionFighterPlayer.setPosition(new Vector2(getPosition().x + getCollisionRadius() * 2 + (float)Math.cos(Math.PI * 3) * getSpeed() * delta,
                                getPosition().y + getCollisionRadius() * 2 + (float)Math.sin(Math.PI * 3) * getSpeed() * delta));
                    }
                }

                break;

            case NONE:
                break;

        }


    }

    private void activateBullets(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd){

        bullets.add(currentBullet);

        float shotCount = delta / 0.2f + shotLeftOver;

        int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

        ammoCount = ammoCount - exactShotCount;
        if (ammoCount > 0) {
            shotLeftOver = shotCount - exactShotCount;
        } else {
            shotLeftOver = 0;
        }

        for (int i = 0; i < exactShotCount; i++) {
            currentBullet = new BulletPlayer("bullito", getPosition(), getSpace(), getAngle(), 400, 2f, randomDamageCountBullet());
            toAdd.add(currentBullet);

        }
    }

    private void activateMissiles(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd){
        missiles.add(currentMissile);

        float missileCount = delta / 0.5f + missileLeftOver;

        int exactMissileCount = Math.min(Math.round(missileCount), missileAmmoCount);

        missileAmmoCount = missileAmmoCount - exactMissileCount;
        if (missileAmmoCount > 0) {
            missileLeftOver = missileCount - exactMissileCount;
        } else {
            missileLeftOver = 0;
        }
        for (int i = 0; i < exactMissileCount; i++) {
            currentMissile = new MissilePlayer("missilito", getPosition(), getSpace(), getAngle(), 400, 5f, randomDamageCountMissile());
            toAdd.add(currentMissile);
        }

    }

    private void activateSpaceMines(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd){
        spaceMines.add(currentSpaceMine);

        float minesCount = delta / 2.0f + minesLeftOver;

        int exactMineCount = Math.min(Math.round(minesCount), mineAmmoCount);

        mineAmmoCount = mineAmmoCount - exactMineCount;
        if (mineAmmoCount > 0) {
            minesLeftOver = minesCount - exactMineCount;
        } else {
            minesLeftOver = 0;
        }

        for (int i = 0; i < exactMineCount; i++) {
            currentSpaceMine = new SpaceMinePlayer("mine", getPosition(), getSpace(), getAngle(), 300, 8f, randomDamageCountMine());
            toAdd.add(currentSpaceMine);
        }
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public int getMineAmmoCount() {
        return mineAmmoCount;
    }

    public float getSpeed() {
        return speed;
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
        }else if (switchWeaponState == SwitchWeaponState.MISSILES){
            result.put("ammoCount", getMissileAmmoCount());
        }else if (switchWeaponState == SwitchWeaponState.SPACE_MINES){
            result.put("ammoCount", getMineAmmoCount());
        }else {
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
    public Map<String, Object> getMissileProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("missileCount", missileAmmoCount);

        return result;
    }

    @Override
    public Map<String, Object> getRandomProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("switchWeaponState", switchWeaponState);

        return result;
    }
}

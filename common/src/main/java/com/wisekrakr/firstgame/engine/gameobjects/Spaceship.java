package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.Vector2;

import com.wisekrakr.firstgame.engine.SpaceEngine;

import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpShield;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;
import com.wisekrakr.firstgame.engine.gameobjects.spaceshipparts.Exhaust;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BeamCannon;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BulletPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.MissilePlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Shield;

import java.util.*;

public class Spaceship extends GameObject {

    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    public SpecialPowerState powerState = SpecialPowerState.NO_POWER;
    private ShootingState shootingState = ShootingState.PACIFIST;
    private AimingState aimingState = AimingState.NONE;

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

    private BeamCannon beamCannon;

    private float lastDodge = -100000f;

    public Spaceship(String name, Vector2 position, SpaceEngine space) {
        super(name, position, space);

        ammoCount = 10000;
        missileAmmoCount = 10;
        health = 1000;
        score = 0;

        setCollisionRadius(20f);

        bullets = new ArrayList<>();
        missiles = new ArrayList<>();
    }

    public void modifySpeed(float v) {
        speedX = speedX * v;
        speedY = speedY * v;
    }

    public enum ThrottleState {
        REVERSE, STATUSQUO, FORWARDS, FULL_STOP
    }

    public enum SteeringState {
        LEFT, CENTER, RIGHT
    }

    public enum SpecialPowerState {
        NO_POWER, BOOSTING, ULTRA_DODGE, BEAM
    }

    public enum ShootingState {
        PACIFIST, FIRING, MISSILE_FIRING
    }

    public enum AimingState {
        LEFT_BEAM_ANGLE, RIGHT_BEAM_ANGLE, NONE
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {

        // angle = angle + (float) Math.PI;
    }

    public void control(ThrottleState throttle, SteeringState steering, SpecialPowerState powerState, ShootingState shootingState, AimingState aimingState) {
        this.throttle = throttle;
        this.steering = steering;
        this.powerState = powerState;
        this.shootingState = shootingState;
        this.aimingState = aimingState;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Enemy) {
            toDelete.add(subject);
            Random random = new Random();
            int debrisParts = random.nextInt(10) + 1;
            for (int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", subject.getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 30, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * ((Enemy) subject).getRadius()));

            }
        }
        if (subject instanceof PowerUpShield) {
            toDelete.add(subject);
            toAdd.add(new Shield("shield", getPosition(), getSpace(), getAngle(), this.getCollisionRadius() * 2));
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
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
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
                speedX = speedX + delta * 280f * (float) Math.cos(angle);
                speedY = speedY + delta * 280f * (float) Math.sin(angle);

                toAdd.add(new Exhaust("exhaust", this.getPosition(), getSpace(), -this.getOrientation(), getCollisionRadius() / 5));
                break;

            case REVERSE:
                speedX = speedX - delta * 280f * (float) Math.cos(angle);
                speedY = speedY - delta * 280f * (float) Math.sin(angle);

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

        float speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);

        if (speed > 400) {
            speedX = speedX * 400 / speed;
            speedY = speedY * 400 / speed;
        }


        switch (powerState) {
            case BOOSTING:
                speedX = speedX + (float) Math.cos(angle) * Math.min(speed + 400, 600);
                speedY = speedY + (float) Math.sin(angle) * Math.min(speed + 400, 600);

                speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
                if (speed > 600) {
                    speedX = speedX * 600 / speed;
                    speedY = speedY * 600 / speed;
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

            case BEAM:
                beamCannon = new BeamCannon("beam", getPosition(), getSpace(), getAngle(), getCollisionRadius()/4);
                toAdd.add(beamCannon);
                break;
        }

//        speed = Math.min(speed + delta * 280f, 400);

        /*
        if (Math.signum(oldSpeed) == -Math.signum(speed)) {
            speed = 0;
        }
        */

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
                    currentBullet = new BulletPlayer("bullito", getPosition(), getSpace(), getAngle(), 400, 2f);
                    currentBullet.setDamage(currentBullet.randomDamageCountBullet());
                    toAdd.add(currentBullet);

                }
                break;
            case MISSILE_FIRING:
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
                    currentMissile = new MissilePlayer("missilito", getPosition(), getSpace(), getAngle(), 200, 5f);
                    currentMissile.setDamage(currentMissile.randomDamageCountMissile());
                    toAdd.add(currentMissile);
                }

                break;

            case PACIFIST:
                shotLeftOver = 0;
                break;
        }

        switch (aimingState) {

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


    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("distanceTravelled", distanceTravelled);

        return result;
    }

    @Override
    public Map<String, Object> getAmmoProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("ammoCount", ammoCount);

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
}

package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.MissileEnemy;
import com.wisekrakr.firstgame.engine.gameobjects.powerups.PowerUpMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.PlayerMissile;

import java.util.*;

public abstract class Spaceship extends GameObject {
    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    private SpecialPowerState powerState = SpecialPowerState.NO_POWER;
    private ShootingState shootingState = ShootingState.PACIFIST;

    private float speed = 0;
    private float angle = (float) Math.PI / 2;
    private float distanceTravelled = 0;
    private int ammoCount;
    private int missileAmmoCount;
    private float shotLeftOver;
    private float missileLeftOver;
    private int health;
    private int score;


    public Spaceship(String name, Vector2 position, SpaceEngine space) {
        super(name, position, space);
        ammoCount = 10000;
        missileAmmoCount = 100;
        health = 1000;
        score = 0;
        setCollisionRadius(10f);

    }

    public void resetControl() {
        speed = 0;
        angle = (float) Math.PI / 2;
    }

    public enum ThrottleState {
        REVERSE, STATUSQUO, FORWARDS
    }

    public enum SteeringState {
        LEFT, CENTER, RIGHT
    }

    public enum SpecialPowerState {
        NO_POWER, BOOSTING, ULTRA_DODGE
    }

    public enum ShootingState {
        PACIFIST, FIRING, MISSILE_FIRING
    }

    public void control(ThrottleState throttle, SteeringState steering, SpecialPowerState powerState, ShootingState shootingState) {
        this.throttle = throttle;
        this.steering = steering;
        this.powerState = powerState;
        this.shootingState = shootingState;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject != null){
            toDelete.add(subject);
        }


    }



    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        switch (steering) {
            case LEFT:
                angle = angle + delta * 3f;
                break;
            case RIGHT:
                angle = angle - delta * 3f;
                break;
        }

        float oldSpeed = speed;

        switch (throttle) {
            case FORWARDS:
                speed = Math.min(speed + delta * 200f, 350);
                break;
            case REVERSE:
                speed = Math.max(speed - delta * 105f, -180);
                break;
        }

        if (Math.signum(oldSpeed) == -Math.signum(speed)) {
            speed = 0;
        }

        distanceTravelled = distanceTravelled + Math.abs(delta * speed);

        if (health <= 0) {
            toDelete.add(this);
        }

        setPosition(new Vector2(
                getPosition().x + delta * speed * (float) Math.cos(angle),
                getPosition().y + delta * speed * (float) Math.sin(angle)
        ));

        setOrientation(angle);

        switch (powerState) {
            case BOOSTING:
                speed = Math.min(speed + delta * 300f, 500);
                break;
            case ULTRA_DODGE:
                Random random = new Random();
                setPosition(new Vector2(getPosition().x + random.nextFloat() * getCollisionRadius(),
                        getPosition().y + +random.nextFloat() * getCollisionRadius()));

                break;
        }

        switch (shootingState) {
            case FIRING:

                float shotCount = delta / 0.1f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(new Bullet("bullito", new Vector2(getPosition().x + getAngle(), getPosition().y + getAngle()),
                            getSpace(), getAngle(), 400, 0.1f));

                }

                break;

            case MISSILE_FIRING:

                float missileCount = delta / 0.5f + missileLeftOver;

                int exactMissileCount = Math.min(Math.round(missileCount), missileAmmoCount);

                missileAmmoCount = missileAmmoCount - exactMissileCount;
                if (missileAmmoCount > 0) {
                    missileLeftOver = missileCount - exactMissileCount;
                } else {
                    missileLeftOver = 0;
                }
                for (int i = 0; i < exactMissileCount; i++) {
                    toAdd.add(new PlayerMissile("missilito", new Vector2(getPosition().x + getAngle(), getPosition().y + getAngle()),
                            getSpace(), getAngle(), 200, 5f));
                }

                break;

            case PACIFIST:
                shotLeftOver = 0;
                break;

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

    public float getSpeed() {
        return speed;
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

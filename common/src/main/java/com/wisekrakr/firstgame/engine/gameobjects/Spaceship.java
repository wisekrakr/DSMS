package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.Set;

public abstract class Spaceship extends GameObject {
    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    private SpecialPowerState powerState = SpecialPowerState.NO_POWER;
    public ShootingState shootingState = ShootingState.PACIFIST;

    private float speed = 0;
    private float angle = (float) Math.PI / 2;
    private Integer distanceTravelled = 0;
    private float ammoCount;

    private static final float DEFAULT_BULLET_SPEED = 80;

    public Spaceship(String name, Vector2 position, SpaceEngine space) {
        super(name, position, space);
        ammoCount = 10000;
        setCollisionRadius(10);
    }

    public Spaceship getSpaceship(){

        return this;
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
        PACIFIST, FIRING;
    }


    public void control(ThrottleState throttle, SteeringState steering, SpecialPowerState powerState, ShootingState shootingState) {
        this.throttle = throttle;
        this.steering = steering;
        this.powerState = powerState;
        this.shootingState = shootingState;
    }

    @Override
    public void elapseTime(float delta) {
        switch (steering) {
            case LEFT:
                angle = angle + delta * 2f;
                break;
            case RIGHT:
                angle = angle - delta * 2f;
                break;
        }

        float oldSpeed = speed;

        switch (throttle) {
            case FORWARDS:
                speed = Math.min(speed + delta * 105f, 200);
                break;
            case REVERSE:
                speed = Math.max(speed - delta * 65f, -100);
                break;
        }

        if (Math.signum(oldSpeed) == -Math.signum(speed)) {
            speed = 0;
        }

        setPosition(new Vector2(
                getPosition().x + delta * speed * (float) Math.cos(angle),
                getPosition().y + delta * speed * (float) Math.sin(angle)
        ));

        setOrientation(angle);

        switch (powerState){
            case BOOSTING:
                speed = Math.min(speed + delta * 200f, 500);
                break;
            case ULTRA_DODGE:
                setPosition(new Vector2(getPosition().x +10, getPosition().y + 10));

                break;
        }

        switch (shootingState){
            case FIRING:


                ammoCount--;
                break;

            case PACIFIST:
                break;

        }
    }

    @Override
    public void shootingBullets(GameObject bullet, Set<GameObject> toAdd, Set<GameObject> toDelete) {
        toAdd.add(bullet);

    }

    private Bullet createBullet(){

        return new Bullet("Bullito", this.getPosition(), getSpace(), this.getAngle(), DEFAULT_BULLET_SPEED, 2f);

    }

    @Override
    public void attack(GameObject target) {

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

    public float getAmmoCount() {
        return ammoCount;
    }



    @Override
    public Integer getDistanceTravelled() {
        return distanceTravelled;
    }

}

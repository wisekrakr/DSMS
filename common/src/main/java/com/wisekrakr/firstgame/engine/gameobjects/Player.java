package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.SpaceshipControlRequest;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractGameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.BulletCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.HomingMissileCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.AbstractPhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Random;

public class Player extends AbstractGameCharacter {
    private final double maxHealth;
    private String name;
    private PhysicalObject spaceship;
    private SpaceshipControlRequest lastControl;

    private double health = 100;
    private float angle;
    private float lastDodge = -1000f;
    private final float defaultSpeed = 100f;
    private final float maxSpeed = 175f;
    private float shootTime;
    private float adaptedAngle;


    public Player(String name) {
        this.name = name;
        maxHealth = health;
    }

    @Override
    public void start() {
        float startDirection = GameHelper.randomDirection();

        spaceship = getContext().addPhysicalObject(name , GameHelper.randomPosition(), startDirection, 0, startDirection,
                Visualizations.SPACESHIP, 10f, new AbstractPhysicalObjectListener() {
                });

        getContext().updatePhysicalObjectExtra(spaceship, "radius", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "distanceTravelled", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "score", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "switchWeaponState", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "ammoCount", 10 );
        getContext().updatePhysicalObjectExtra(spaceship, "health", health);
        getContext().updatePhysicalObjectExtra(spaceship, "maxHealth", maxHealth);
        getContext().updatePhysicalObjectExtra(spaceship, "healthPercentage", 1d);


    }

    public void control(SpaceshipControlRequest request) {
        this.lastControl = request;
    }



    @Override
    public void elapseTime(float delta) {
        if (spaceship == null) {
            return;
        }

        if (health <= 0) {
            getContext().removePhysicalObject(spaceship);
            spaceship = null;

            return;
        }

        if (lastControl == null) {
            return;
        }

        float speed = spaceship.getSpeedMagnitude();
        float speedX = (float) Math.cos(spaceship.getSpeedDirection()) * speed;
        float speedY = (float) Math.sin(spaceship.getSpeedDirection()) * speed;

        switch (lastControl.getSteeringState()) {
            case LEFT:
                angle = angle + 3f * delta;
                break;
            case RIGHT:
                angle = angle - 3f * delta;
                break;
        }

        switch (lastControl.getThrottleState()) {
            case FORWARDS:
                speedX = speedX + delta * defaultSpeed * (float) Math.cos(spaceship.getOrientation());
                speedY = speedY + delta * defaultSpeed * (float) Math.sin(spaceship.getOrientation());
                break;

            case REVERSE:
                speedX = speedX - delta * defaultSpeed * (float) Math.cos(spaceship.getOrientation());
                speedY = speedY - delta * defaultSpeed * (float) Math.sin(spaceship.getOrientation());
                break;

            case FULL_STOP:
                speedX = 0;
                speedY = 0;
                break;
        }

        speed = (float) Math.min(Math.sqrt(speedX * speedX + speedY * speedY), defaultSpeed);

        if (speed > (defaultSpeed + (defaultSpeed / 2))) {
            speedX = speedX * (defaultSpeed + (defaultSpeed / 2)) / speed;
            speedY = speedY * (defaultSpeed + (defaultSpeed / 2)) / speed;
        }


        switch (lastControl.getSpecialPowerState()) {
            case BOOSTING:
                speedX = speedX + (float) Math.cos(spaceship.getOrientation()) * Math.min(speed + (defaultSpeed + (defaultSpeed / 2)), maxSpeed);
                speedY = speedY + (float) Math.sin(spaceship.getOrientation()) * Math.min(speed + (defaultSpeed + (defaultSpeed / 2)), maxSpeed);

                speed = (float) Math.min(Math.sqrt(speedX * speedX + speedY * speedY), maxSpeed);
                if (speed > maxSpeed) {
                    speedX = speedX * maxSpeed / speed;
                    speedY = speedY * maxSpeed / speed;
                }
                break;

            case ULTRA_DODGE:
                if (getContext().getSpaceEngine().getTime() - lastDodge > 10) {
                    lastDodge = getContext().getSpaceEngine().getTime();
                    Random random = new Random();


                    getContext().updatePhysicalObject(
                            spaceship,
                            null,
                            new Vector2(
                                    spaceship.getPosition().x + delta * speed * (spaceship.getOrientation() + random.nextFloat() * 200 - 100),
                                    spaceship.getPosition().y + delta * speed * (spaceship.getOrientation() + random.nextFloat() * 200 - 100)
                            ),
                            null,
                            null,
                            null,
                            null,
                            null);
                }

                break;
        }

        float direction = (float) Math.atan2((double) speedY, (double) speedX);

        getContext().updatePhysicalObject(
                spaceship,
                null,
                null,
                angle,
                speed,
                direction,
                (getContext().getSpaceEngine().getTime() - lastDodge > 10)?Visualizations.SPACESHIP:Visualizations.BOULDER,
                null
                );



        //distanceTravelled = distanceTravelled + Math.abs(delta * speed);

        float x = spaceship.getPosition().x;
        float y = spaceship.getPosition().y;

        float deltaX = ((float) Math.cos(spaceship.getOrientation()));
        float deltaY = ((float) Math.sin(spaceship.getOrientation()));

        switch (lastControl.getShootingState()) {
            case FIRING:

                adaptedAngle = (float) Math.atan2(deltaY * 200 + speedY, deltaX * 200 + speedX);

                shootTime += delta;

                if (shootTime > 0.3f){
                    getContext().addCharacter(new BulletCharacter(new Vector2(x + spaceship.getCollisionRadius() * deltaX,
                            y + spaceship.getCollisionRadius() * deltaY),
                            200f,
                            adaptedAngle,
                            3f,
                            1,
                            3f,
                            Visualizations.LEFT_CANNON
                    ));
                    shootTime = 0f;
                }

                break;
            case MISSILE_FIRING:

                adaptedAngle = (float) Math.atan2(deltaY * 200 + speedY, deltaX * 200 + speedX);

                shootTime += delta;

                if (shootTime > 0.3f){
                    getContext().addCharacter(new HomingMissileCharacter(new Vector2(x + spaceship.getCollisionRadius() * deltaX,
                            y + spaceship.getCollisionRadius() * deltaY),
                            200f,
                            adaptedAngle,
                            3f,
                            1,
                            3f,
                            Visualizations.RIGHT_CANNON,
                            getContext()
                    ));
                    shootTime = 0f;
                }


                break;
            case PLACE_MINE:
                break;
            case PACIFIST:
                break;
        }
/*
        if (mouseAiming != null) {
            shootTime -= delta * 0.5f;

            float x = getPosition().x;
            float y = getPosition().y;

            float deltaX = ((float) Math.cos(getOrientation()));
            float deltaY = ((float) Math.sin(getOrientation()));

            adaptedAngle = (float) Math.atan2(deltaY * 200 + speedY, deltaX * 200 + speedX);

            if (shootTime < 0) {
                if (switchWeaponState == Spaceship.SwitchWeaponState.BULLETS) {
                    BulletObject b = new BulletObject(
                            new Vector2(x + getCollisionRadius() * deltaX,
                                    y + getCollisionRadius() * deltaY), adaptedAngle + mouseAiming, this);

                    toAdd.add(b);
                    ammoCount--;
                } else if (switchWeaponState == Spaceship.SwitchWeaponState.MISSILES) {
                    MissileObject m = new MissileObject(
                            new Vector2(x + getCollisionRadius(), y + getCollisionRadius()),
                            adaptedAngle + mouseAiming, missileTarget, this);

                    toAdd.add(m);
                    missileAmmoCount--;
                }
                shootTime = 0.001f;
            }
            mouseAiming = null;
        }
        */


    }
}

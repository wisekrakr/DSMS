package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.SpaceshipControlRequest;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractGameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.BulletCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.HomingMissileCharacter;
import com.wisekrakr.firstgame.engine.physicalobjects.AbstractPhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.List;

public class Player extends AbstractGameCharacter {
    private final float maxHealth;
    private String name;
    private PhysicalObject spaceship;
    private SpaceshipControlRequest lastControl;

    private float health = 100f;
    private float angle;
    private float lastDodge = -1000f;
    private float shootTime;
    private float adaptedAngle;
    private PhysicalObject nearestObject;

    public Player(String name) {
        this.name = name;
        maxHealth = health;
    }

    @Override
    public void start() {
        float startDirection = GameHelper.randomDirection();

        spaceship = getContext().addPhysicalObject(name ,
                GameHelper.randomPosition(),
                startDirection,
                0,
                startDirection,
                health,
                0,
                Visualizations.SPACESHIP,
                10f,
                new AbstractPhysicalObjectListener() {
                });

        getContext().updatePhysicalObjectExtra(spaceship, "radius", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "distanceTravelled", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "score", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "switchWeaponState", 10f);
        getContext().updatePhysicalObjectExtra(spaceship, "ammoCount", 10 );
        getContext().updatePhysicalObjectExtra(spaceship, "health", health);
        getContext().updatePhysicalObjectExtra(spaceship, "maxHealth", maxHealth);
        getContext().updatePhysicalObjectExtra(spaceship, "healthPercentage", 1f);
        getContext().updatePhysicalObjectExtra(spaceship, "nearestObject", nearestObject);


    }

    private void findNearestObject() {

        List<NearPhysicalObject> nearbyPhysicalObjects =
                getContext().findNearbyPhysicalObjects(getContext().getPhysicalObject(), (float) Double.POSITIVE_INFINITY);

        if (!nearbyPhysicalObjects.isEmpty()) {
            for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                String name = nearPhysicalObject.getObject().getName();
                if (!name.contains("weapon") && !name.contains("debris")) {
                    nearestObject = nearPhysicalObject.getObject();
                }
            }
        }
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

        findNearestObject();
        //System.out.println(nearestObject.getName());

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

        float defaultSpeed = 100f;
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
                float maxSpeed = 175f;
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

                    getContext().updatePhysicalObject(
                            spaceship,
                            null,
                            new Vector2(
                                    spaceship.getPosition().x + delta * speed * (spaceship.getOrientation() + GameHelper.randomGenerator.nextFloat() * 200 - 100),
                                    spaceship.getPosition().y + delta * speed * (spaceship.getOrientation() + GameHelper.randomGenerator.nextFloat() * 200 - 100)
                            ),
                            null,
                            null,
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
                null,
                null,
                (getContext().getSpaceEngine().getTime() - lastDodge > 10) ? Visualizations.SPACESHIP : Visualizations.BOULDER,
                null
        );


        //distanceTravelled = distanceTravelled + Math.abs(delta * speed);

        float deltaX = ((float) Math.cos(spaceship.getOrientation()));
        float deltaY = ((float) Math.sin(spaceship.getOrientation()));

        switch (lastControl.getShootingState()) {
            case FIRING:
                fireBullet(deltaX, speedX, deltaY, speedY, delta);

                break;
            case MISSILE_FIRING:
                fireHomingMissile(deltaX, speedX, deltaY, speedY, delta);

                break;
            case PLACE_MINE:
                break;
            case PACIFIST:
                break;
        }

        if (lastControl.getMouseAiming() != null){
            adaptedAngle += lastControl.getMouseAiming();
            if (lastControl.getSwitchWeaponState() == Spaceship.SwitchWeaponState.BULLETS){
                fireBullet(deltaX, speedX, deltaY, speedY, delta);
            }else if (lastControl.getSwitchWeaponState() == Spaceship.SwitchWeaponState.MISSILES){
                fireHomingMissile(deltaX, speedX, deltaY, speedY, delta);
            }
        }

    }

    private void fireBullet(float deltaX, float speedX, float deltaY, float speedY, float delta){
        adaptedAngle = (float) Math.atan2(deltaY * 200f + speedY, deltaX * 200f + speedX);

        if (lastControl.getMouseAiming() != null){
            adaptedAngle += lastControl.getMouseAiming();
        }

        shootTime += delta;

        if (shootTime > 0.1f) {
            getContext().addCharacter(new BulletCharacter(spaceship.getPosition(),
                    200f,
                    adaptedAngle,
                    3f,
                    3f,
                    5f,
                    Visualizations.LEFT_CANNON,
                    getContext()
            ));
            shootTime = 0f;
        }
    }

    private void fireHomingMissile(float deltaX, float speedX, float deltaY, float speedY, float delta){
        adaptedAngle = (float) Math.atan2(deltaY * 120f + speedY, deltaX * 120f + speedX);

        if (lastControl.getMouseAiming() != null){
            adaptedAngle += lastControl.getMouseAiming();
        }

        shootTime += delta;

        if (shootTime > 0.3f) {
            getContext().addCharacter(new HomingMissileCharacter(spaceship.getPosition(),
                    120f,
                    adaptedAngle,
                    3f,
                    10f,
                    3f,
                    200f,
                    Visualizations.RIGHT_CANNON,
                    getContext()
            ));
            shootTime = 0f;
        }
    }
}

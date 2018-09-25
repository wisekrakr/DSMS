package com.wisekrakr.firstgame.engine.gamecharacters;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.MissileObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.SpaceMineObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.*;

public class SpaceshipCharacter extends AbstractGameCharacter {

    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    private SpecialPowerState powerState = SpecialPowerState.NO_POWER;
    private ShootingState shootingState = ShootingState.PACIFIST;
    private Float mouseAiming;
    private SwitchWeaponState switchWeaponState = SwitchWeaponState.NONE;

    private Float hardSteering;
    private float speedX = 0;
    private float speedY = 0;
    private float distanceTravelled = 0;
    private float lastDodge = -100000f;

    private float speed;
    private float defaultSpeed = 60f;
    private float maxSpeed = 125f;
    private float angle;
    private float direction;
    private Vector2 position;
    private PhysicalObject player;

    public SpaceshipCharacter(Vector2 position) {
        this.position = position;
    }

    @Override
    public void start() {
        player = getContext().addPhysicalObject("Player",
                position,
                0,0,0,
                Visualizations.PLAYER,
                10f,
                null);

        getContext().updatePhysicalObjectExtra(player, "radius", 10f);
    }

    @Override
    public void elapseTime(float delta) {
        if (hardSteering != null) {
            //angle = (float) Math.atan2(Gdx.input.getY()- getPosition().y, Gdx.input.getX()- getPosition().x);
            //angle = angle + hardSteering * delta;
            angle = hardSteering;

            hardSteering = null;
        } else {
            switch (steering) {
                case LEFT:
                    angle = angle + 3f * delta;
                    getContext().updatePhysicalObject(player,
                            null, null,
                            angle,
                            null, null, null, null);
                    break;
                case RIGHT:
                    angle = angle - 3f * delta;
                    getContext().updatePhysicalObject(player,
                            null, null,
                            angle,
                            null, null, null, null);
                    break;
            }
        }
        angle = player.getOrientation();
        switch (throttle) {
            case FORWARDS:
                speedX = speedX + delta * defaultSpeed * (float) Math.cos(angle);
                speedY = speedY + delta * defaultSpeed * (float) Math.sin(angle);
                speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);

                getContext().updatePhysicalObject(player,
                        null, null, null,
                        speed,
                        null, null, null);
                break;

            case REVERSE:
                speedX = speedX - delta * defaultSpeed * (float) Math.cos(angle);
                speedY = speedY - delta * defaultSpeed * (float) Math.sin(angle);
                speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);

                getContext().updatePhysicalObject(player,
                        null, null, null,
                        speed,
                        null, null, null);
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
                getContext().updatePhysicalObject(player,
                        null, null, null,
                        0f,
                        null, null, null);
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

                //toAdd.add(new Exhaust("exhaust", getPosition(), -this.getOrientation(), getCollisionRadius() / 3));
                break;

            case ULTRA_DODGE:
                if (getContext().getSpaceEngine().getTime() - lastDodge > 10) {
                    lastDodge = getContext().getSpaceEngine().getTime();
                    Random random = new Random();
                    position = new Vector2(
                            position.x + delta * speed * (angle + random.nextFloat() * 200 - 100),
                            position.y + delta * speed * (angle + random.nextFloat() * 200 - 100)
                    );
                }

                break;
        }

        //Player movement
        position = new Vector2(
                position.x + delta * speedX,
                position.y + delta * speedY
        );
        position = player.getPosition();

        direction = (float) Math.atan2((double) speedY, (double) speedX);
        direction = player.getSpeedDirection();

        distanceTravelled = distanceTravelled + Math.abs(delta * speed);
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

    private float fireRate(float rateOfFire) {
        return rateOfFire;
    }

    public void control(ThrottleState throttle, SteeringState steering, SpecialPowerState powerState, ShootingState shootingState,
                        Float aimingState, SwitchWeaponState switchWeaponState, Float hardSteering) {
        this.throttle = throttle;
        this.steering = steering;
        this.powerState = powerState;
        this.shootingState = shootingState;
        this.mouseAiming = aimingState;
        this.switchWeaponState = switchWeaponState;
        this.hardSteering = hardSteering;
    }



    private boolean collisionDetected(PhysicalObject object1, PhysicalObject object2) {
        return
                Math.sqrt(
                        (((object1.getPosition().x) - (object2.getPosition().x)))
                                * ((object1.getPosition().x) - (object2.getPosition().x))
                                + ((object1.getPosition().y) - (object2.getPosition().y))
                                * ((object1.getPosition().y) - (object2.getPosition().y)))
                        < (object1.getCollisionRadius() + object2.getCollisionRadius());

    }

    public void scoringSystem(PhysicalObject enemy, PhysicalObject subject) {

        if (enemy instanceof NonPlayerCharacter && !(enemy instanceof WeaponObjectClass)) {
            if (subject instanceof BulletObject || subject instanceof MissileObject || subject instanceof SpaceMine) {
                if (collisionDetected(enemy, subject)) {
                    if (subject instanceof BulletObject) {
                       // this.setScore((float) (this.getScore() + subject.getDamage()));
                        //if (enemy.getHealth() <= 0) {
                        //    this.setScore(this.getScore() + 50);
                       // }
                    }
                    if (subject instanceof MissileObject) {
                       // this.setScore((float) (this.getScore() + subject.getDamage()));
                       // if (enemy.getHealth() <= 0) {
                       //     this.setScore(this.getScore() + 100);
                       // }
                    }
                    if (subject instanceof SpaceMine) {
                        //this.setScore((float) (this.getScore() + subject.getDamage()));
                        //if (enemy.getHealth() <= 0) {
                       //     this.setScore(this.getScore() + 200);
                        //}
                    }
                }
            }
        }
    }

    private PhysicalObject missileTarget = null;


    public void nearby(List<PhysicalObject> targets) {
        if (!targets.isEmpty()) {
            missileTarget = targets.get(0);
        }
        else {
            missileTarget = null;
        }
        //System.out.println("Targeting : " + missileTarget);
    }


}

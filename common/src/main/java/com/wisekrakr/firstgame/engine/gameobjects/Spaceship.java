package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;


import java.util.*;

public class Spaceship extends GameObject {

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
    private int ammoCount;
    private int missileAmmoCount;
    private float shotLeftOver;
    private float missileLeftOver;
    private double health;
    private float score;


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

    private boolean shieldActivated;

    private double damageTaken = 0;
    private boolean hit = false;
    private boolean pickedUp = false;
    private double healthPercentage;
    private double maxHealth;
    private float angle;
    private float adaptedAngle;
    private float shootTime = 0.001f;

    public Spaceship(String name, Vector2 position) {
        super(GameObjectVisualizationType.SPACESHIP, name, position);

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


    private double healthInPercentages() {
        if (isHit()) {
            double z = (getHealth() - getDamageTaken());
            healthPercentage = (z / maxHealth * 100);
            healthPercentage /= 100;
        }
        return healthPercentage;
    }

    private float fireRate(float rateOfFire) {
        return rateOfFire;
    }

//    public void control(ThrottleState throttle, SteeringState steering, SpecialPowerState powerState, ShootingState shootingState,
//                        Float aimingState, SwitchWeaponState switchWeaponState, Float hardSteering) {
//        this.throttle = throttle;
//        this.steering = steering;
//        this.powerState = powerState;
//        this.shootingState = shootingState;
//        this.mouseAiming = aimingState;
//        this.switchWeaponState = switchWeaponState;
//        this.hardSteering = hardSteering;
//    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Mission) {
            pickedUp = true;
        }
    }

    private boolean collisionDetected(GameObject object1, GameObject object2) {
        return
                Math.sqrt(
                        (((object1.getPosition().x) - (object2.getPosition().x)))
                                * ((object1.getPosition().x) - (object2.getPosition().x))
                                + ((object1.getPosition().y) - (object2.getPosition().y))
                                * ((object1.getPosition().y) - (object2.getPosition().y)))
                        < (object1.getCollisionRadius() + object2.getCollisionRadius());

    }

    private GameObject missileTarget = null;

    @Override
    public void nearby(List<GameObject> targets) {
        if (!targets.isEmpty()) {
            missileTarget = targets.get(0);
        }
        else {
            missileTarget = null;
        }
        //System.out.println("Targeting : " + missileTarget);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        //Player gets deleted when health is 0
        if (health <= 0) {
            toDelete.add(this);
        }

        if (hardSteering != null) {
            //angle = (float) Math.atan2(Gdx.input.getY()- getPosition().y, Gdx.input.getX()- getPosition().x);
            //angle = angle + hardSteering * delta;
            setOrientation(hardSteering);
            hardSteering = null;
        } else {
            switch (steering) {
                case LEFT:
                    angle = angle + 3f * delta;
                    setOrientation(angle);
                    break;
                case RIGHT:
                    angle = angle - 3f * delta;
                    setOrientation(angle);
                    break;
            }
        }

        switch (throttle) {
            case FORWARDS:
                speedX = speedX + delta * defaultSpeed * (float) Math.cos(getOrientation());
                speedY = speedY + delta * defaultSpeed * (float) Math.sin(getOrientation());
                //toAdd.add(new Exhaust("exhaust", this.getPosition(), -this.getOrientation(), getCollisionRadius() / 5));
                break;

            case REVERSE:
                speedX = speedX - delta * defaultSpeed * (float) Math.cos(getOrientation());
                speedY = speedY - delta * defaultSpeed * (float) Math.sin(getOrientation());
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
                speedX = speedX + (float) Math.cos(getOrientation()) * Math.min(speed + (defaultSpeed + (defaultSpeed / 2)), maxSpeed);
                speedY = speedY + (float) Math.sin(getOrientation()) * Math.min(speed + (defaultSpeed + (defaultSpeed / 2)), maxSpeed);

                speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
                if (speed > maxSpeed) {
                    speedX = speedX * maxSpeed / speed;
                    speedY = speedY * maxSpeed / speed;
                }

                //toAdd.add(new Exhaust("exhaust", getPosition(), -this.getOrientation(), getCollisionRadius() / 3));
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

        setDirection((float) Math.atan2((double) speedY, (double) speedX));

        distanceTravelled = distanceTravelled + Math.abs(delta * speed);


        switch (shootingState) {
            case FIRING:
                activateBullets(delta);
                break;
            case MISSILE_FIRING:
                activateMissiles(delta);
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

        if (mouseAiming != null){
            shootTime -= delta * 0.5f;

            float deltaX = ((float) Math.cos(getOrientation()));
            float deltaY = ((float) Math.sin(getOrientation()));

            adaptedAngle = (float) Math.atan2(deltaY * 200 + speedY, deltaX * 200 + speedX);

            if (shootTime < 0) {
                if (switchWeaponState == SwitchWeaponState.BULLETS){

                    ammoCount--;
                }else if (switchWeaponState == SwitchWeaponState.MISSILES){

                    missileAmmoCount--;
                }
                shootTime = 0.001f;
            }
            mouseAiming = null;
        }
    }

    private void activateBullets(float delta) {
        float shotCount = delta / fireRate(0.2f) + shotLeftOver;

        int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

        ammoCount = ammoCount - exactShotCount;
        if (ammoCount > 0) {
            shotLeftOver = shotCount - exactShotCount;
        } else {
            shotLeftOver = 0;
        }

        float x = getPosition().x;
        float y = getPosition().y;

        float bulletSpeed = 200;

        float deltaX = ((float) Math.cos(getOrientation()));
        float deltaY = ((float) Math.sin(getOrientation()));

        adaptedAngle = (float) Math.atan2(deltaY * bulletSpeed + speedY, deltaX * bulletSpeed + speedX);

        for (int i = 0; i < exactShotCount; i++) {


        }

    }

    private void activateMissiles(float delta) {

        float missileCount = delta / fireRate(0.5f) + missileLeftOver;

        int exactMissileCount = Math.min(Math.round(missileCount), missileAmmoCount);

        missileAmmoCount = missileAmmoCount - exactMissileCount;
        if (missileAmmoCount > 0) {
            missileLeftOver = missileCount - exactMissileCount;
        } else {
            missileLeftOver = 0;
        }

        float x = getPosition().x;
        float y = getPosition().y;

        float deltaX = ((float) Math.cos(getOrientation()));
        float deltaY = ((float) Math.sin(getOrientation()));


        for (int i = 0; i < exactMissileCount; i++) {

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

        }
    }


    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    public int getMissileAmmoCount() {
        return missileAmmoCount;
    }

    public void setMissileAmmoCount(int missileAmmoCount) {
        this.missileAmmoCount = missileAmmoCount;
    }

    public void setMineAmmoCount(int mineAmmoCount) {
        this.mineAmmoCount = mineAmmoCount;
    }

    public int getAmmoCount() {
        return ammoCount;
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

    public double getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(double damageTaken) {
        this.damageTaken = damageTaken;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
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

        result.put("healthPercentage", healthInPercentages());

        result.put("damageTaken", damageTaken);

        result.put("maxHealth", maxHealth);

        result.put("hit", hit);

        result.put("pickedUp", pickedUp);

        result.put("speed", speed);

        result.put("direction", getDirection());

        return result;
    }
}

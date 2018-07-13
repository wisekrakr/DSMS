package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;

import java.util.Set;


public class EnemyHuggyFace extends Enemy {

    private int ammoCount;
    private float shotLeftOver;
    private float time;
    private float changeDirectionTime;
    private Vector2 targetVector;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;
    private float rotationAngle;
    private float rotationSpeed;

    /*
        The "Facehugging" enemy. If the player is in aggro range (500f) it will fly towards the player and when it is within
        attack range (200f) it will rotate around the player and shoot the player constantly with bullets
     */

    public EnemyHuggyFace(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;

        changeDirectionTime = 3f;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            ((Player) subject).setHealth(((Player) subject).getHealth() - 15);
        }
        if (subject instanceof Bullet) {
            if (((Bullet) subject).isPlayerBullet()) {
                float angle = angleBetween(this, subject);
                movingState = MovingState.DEFAULT_FORWARDS;
                setOrientation(angle);
                setDirection(angle);
                setHit(true);
                setDamageTaken(subject.getDamage());
                toDelete.add(subject);
            }
        }
        if (subject instanceof HomingMissile){
            if(((HomingMissile) subject).isPlayerMissile()){
                float angle = angleBetween(this, subject);
                movingState = MovingState.DEFAULT_FORWARDS;
                setOrientation(angle);
                setDirection(angle);
                setHit(true);
                setDamageTaken(subject.getDamage());
                toDelete.add(subject);
            }
        }
        if (subject instanceof SpaceMine){
            if(((SpaceMine) subject).isPlayerMine()){
                setHit(true);
                setDamageTaken(subject.getDamage());
            }
        }
    }

    private enum AttackState {
        PACIFIST, FIRE_BULLETS, SELF_DESTRUCT
    }

    private enum MovingState {
        DEFAULT_FORWARDS, BACKWARDS, HUGGING
    }

    public void aimAtTarget(GameObject target){
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAggroDistance()) {
                float angle = angleBetween(this, target);
                movingState = MovingState.DEFAULT_FORWARDS;
                setOrientation(angle);
                setDirection(angle);
                targetVector = target.getPosition();
            }else {
                attackState = AttackState.PACIFIST;
            }
        }
    }
    public void attackAtTarget(GameObject target){
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                attackState = AttackState.FIRE_BULLETS;
                movingState = MovingState.HUGGING;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }


    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        /*If the enemy has an float of less than 0 health, the enemy gets destroyed and debris GameObjects are spawned in its place
         * */
        if (getHealth() <= 0){
            toDelete.add(this);
        }

        switch (movingState){
            case DEFAULT_FORWARDS:
                time += delta;
                if(time >= changeDirectionTime){
                    float randomDirection = EnemyMechanics.setRandomDirection();
                    setDirection(randomDirection);
                    time = 0;
                }
                setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * getSpeed() * delta,
                        getPosition().y + (float) Math.sin(getDirection()) * getSpeed() * delta)
                );
                setOrientation(getDirection());
                break;
            case HUGGING:
                rotationAngle += 3f * delta;
                setRotationSpeed(getSpeed()/2);
                if (getHealth() <= getHealth()*(10f/100f )){
                    setRotationSpeed(getSpeed());
                }
                setPosition(new Vector2((float) (getTargetVector().x + Math.cos(rotationAngle) * getRotationSpeed()),
                        (float) (getTargetVector().y + Math.sin(rotationAngle) * getRotationSpeed())));

                setOrientation(getDirection());
                break;
            case BACKWARDS:
                setPosition(new Vector2(getPosition().x - (float) Math.cos(getDirection()) * getSpeed() * delta,
                        getPosition().y - (float) Math.sin(getDirection()) * getSpeed() * delta)
                );
                setOrientation((float) (getDirection() + Math.PI));
                break;
        }
        switch (attackState){
            case FIRE_BULLETS:
                ammoCount = getAmmoCount();
                float shotCount = delta / EnemyMechanics.fireRate(5f) + shotLeftOver;
                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(EnemyMechanics.loadBullet(this));
                }
                break;
            case SELF_DESTRUCT:
                toDelete.add(this);
            case PACIFIST:
                shotLeftOver = 0;
        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public Vector2 getTargetVector() {
        return targetVector;
    }

    public void setTargetVector(Vector2 targetVector) {
        this.targetVector = targetVector;
    }
}

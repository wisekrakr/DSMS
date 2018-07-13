package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;

import java.util.List;
import java.util.Set;

public class EnemyWithMinion extends Enemy{

    private int ammoCount;
    private float shotLeftOver;
    private float time;
    private float changeDirectionTime;

    private double minionAngle;
    private Minion minionShooter;
    private boolean minionActivated = false;
    private float minionRotationSpeed;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;
    private float updatedAngle;

    public EnemyWithMinion(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        changeDirectionTime = 20f;

        minionShooter = EnemyMechanics.initMinion(this);
    }

    public void minionMovement(float delta){
        minionAngle += getMinionRotationSpeed() * delta;
        minionShooter.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * getSpeed() /3),
                (float) (getPosition().y + Math.sin(minionAngle) * getSpeed() /3)));
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
        DEFAULT_FORWARDS, BACKWARDS, FLY_AROUND
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= 500f) {
                float angle = angleBetween(this, target);
                float angleNoAim = angleBetweenNoAim(this, target);
                movingState = MovingState.DEFAULT_FORWARDS;
                setOrientation(angle);
                setDirection(angleNoAim);
            }else {
                attackState = AttackState.PACIFIST;
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= 200f) {
                attackState = AttackState.FIRE_BULLETS;
                movingState = MovingState.FLY_AROUND;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }

    private float updateAngle(float delta){
        time += delta;

        if (time >= changeDirectionTime) {
            updatedAngle = (float) (45 * Math.PI * delta);
            time = 0;
        }

        return updatedAngle;
    }

    @Override
    public void afterRemove(List<GameObject> toAdd, List<GameObject> toRemove) {
        toAdd.add(minionShooter);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        /*If the enemy has an float of less than 0 health, the enemy gets destroyed and debris GameObjects are spawned in its place
         * */
        if (getHealth() <= 0){
            toDelete.add(this);
        }

        if (minionActivated){
            minionShooter.setPosition(getPosition());
        }

        minionMovement(delta);
        if (!(getHealth() <= getHealth()*(20f/100f))){
            setMinionRotationSpeed(getSpeed() / 10);
        }else {
            setMinionRotationSpeed(getSpeed() / 20);
        }
        toAdd.add(minionShooter);

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
            case FLY_AROUND:
                setPosition(new Vector2((getPosition().x + (float) Math.cos(getDirection() + updateAngle(delta)) * getSpeed() * delta ),
                        (getPosition().y + (float) Math.sin(getDirection() + updateAngle(delta)) * getSpeed() * delta))
                );
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
                break;
            case PACIFIST:
                shotLeftOver = 0;
                break;
        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public float getMinionRotationSpeed() {
        return minionRotationSpeed;
    }

    public void setMinionRotationSpeed(float minionRotationSpeed) {
        this.minionRotationSpeed = minionRotationSpeed;
    }

    public boolean isMinionActivated() {
        return minionActivated;
    }

    public void setMinionActivated(boolean minionActivated) {
        this.minionActivated = minionActivated;
    }
}

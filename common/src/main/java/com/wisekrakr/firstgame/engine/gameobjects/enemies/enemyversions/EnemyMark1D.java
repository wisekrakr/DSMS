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

import java.util.Random;
import java.util.Set;

public class EnemyMark1D extends Enemy {

    private int ammoCount;
    private float shotLeftOver;
    private float time;
    private float changeDirectionTime;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;
    private float randomAngle;

    /*
    Mark 1D can fire bullets and fly to the player when in range and when it attacks it dodges. Aggro = 700, Attack = 300
     */

    public EnemyMark1D(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;

        changeDirectionTime = 10f;
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            ((Player) subject).setHealth(((Player) subject).getHealth() - 15);
        }
        if (subject instanceof Bullet) {
            if (((Bullet) subject).isPlayerBullet()) {
                setHit(true);
                setDamageTaken(subject.getDamage());
                toDelete.add(subject);
            }
        }
        if (subject instanceof HomingMissile){
            if(((HomingMissile) subject).isPlayerMissile()){
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
        DEFAULT_FORWARDS, BACKWARDS, DODGING
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= 700f) {
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
            if (distanceBetween(this, target) <= 300f) {
                attackState = AttackState.FIRE_BULLETS;
                movingState = MovingState.DODGING;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }

    public float randomAngle(float delta){
        time += delta;
        Random random = new Random();

        if (time >= getChangeDirectionTime()) {
            randomAngle = (float) (random.nextInt(180) * Math.PI * delta);
            time = 0;
        }
        return randomAngle;
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
            case DODGING:
                setPosition(new Vector2((getPosition().x + (float) Math.cos(getDirection() + randomAngle(delta)) * getSpeed() * delta ),
                        (getPosition().y + (float) Math.sin(getDirection() + randomAngle(delta)) * getSpeed() * delta))
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
            case PACIFIST:
                shotLeftOver = 0;
        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }
}

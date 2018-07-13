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

public class EnemyMark2B extends Enemy {

    private int laserAmmoCount;
    private float laserShotLeftOver;
    private float time;
    private float changeDirectionTime;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;

    /*
    Mark 2A can fire lasers and fly to the player when in range and when attacking, blink towards or away from player. Aggro = 900, Attack = 500
     */

    public EnemyMark2B(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        laserAmmoCount = (int) Double.POSITIVE_INFINITY;
        laserShotLeftOver = laserAmmoCount;

        changeDirectionTime = 7f;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            ((Player) subject).setHealth(((Player) subject).getHealth() - 20);
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
        PACIFIST, FIRE_LASERS, SELF_DESTRUCT
    }

    private enum MovingState {
        DEFAULT_FORWARDS, BACKWARDS, BLINK
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= 900f) {
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
            if (distanceBetween(this, target) <= 500f) {
                attackState = AttackState.FIRE_LASERS;
                movingState = MovingState.BLINK;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        Random random = new Random();

        /*If the enemy has an float of less than 0 health, the enemy gets destroyed and debris GameObjects are spawned in its place
         * */
        if (getHealth() <= 0) {
            toDelete.add(this);
        }

        switch (movingState) {
            case DEFAULT_FORWARDS:
                time += delta;
                if (time >= changeDirectionTime) {
                    float randomDirection = EnemyMechanics.setRandomDirection();
                    setDirection(randomDirection);
                    time = 0;
                }
                setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * getSpeed() * delta,
                        getPosition().y + (float) Math.sin(getDirection()) * getSpeed() * delta)
                );
                setOrientation(getDirection());
                break;
            case BLINK:
                if(time >= 5) {
                    setPosition(new Vector2(
                            getPosition().x + delta * getSpeed() * (random.nextFloat() * 200 - 100),
                            getPosition().y + delta * getSpeed() * (random.nextFloat() * 200 - 100)
                    ));
                    time = 0;
                }
                break;
            case BACKWARDS:
                setPosition(new Vector2(getPosition().x - (float) Math.cos(getDirection()) * getSpeed() * delta,
                        getPosition().y - (float) Math.sin(getDirection()) * getSpeed() * delta)
                );
                setOrientation((float) (getDirection() + Math.PI));
                break;
        }
        switch (attackState){
            case FIRE_LASERS:
                laserAmmoCount = getLaserAmmoCount();
                float laserShotCount = delta / EnemyMechanics.fireRate(5f) + laserShotLeftOver;

                int laserExactShotCount = Math.min(Math.round(laserShotCount), laserAmmoCount);

                laserAmmoCount = laserAmmoCount - laserExactShotCount;
                if (laserAmmoCount > 0) {
                    laserShotLeftOver = laserShotCount - laserExactShotCount;
                } else {
                    laserShotLeftOver = 0;
                }

                for (int i = 0; i < laserExactShotCount; i++) {
                    toAdd.add(EnemyMechanics.loadLaser(this));
                }

                break;
            case SELF_DESTRUCT:
                toDelete.add(this);
            case PACIFIST:
                laserShotLeftOver = 0;
        }
    }

    public int getLaserAmmoCount() {
        return laserAmmoCount;
    }
}

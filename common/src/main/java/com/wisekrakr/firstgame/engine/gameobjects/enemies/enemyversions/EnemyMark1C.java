package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MissileMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;

import java.util.Set;

public class EnemyMark1C extends Enemy{

    private int ammoCount;
    private float shotLeftOver;
    private int missileAmmoCount;
    private float missilesShotLeftOver;
    private float time;
    private float changeDirectionTime;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;

    /*
    Mark 1B can fire bullets or missiles, and fly to the player when in range. Aggro = 500, Attack = 300
     */

    public EnemyMark1C(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;
        missileAmmoCount = (int) Double.POSITIVE_INFINITY;
        missilesShotLeftOver = missileAmmoCount;

        changeDirectionTime = 15f;

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            ((Player) subject).setHealth(((Player) subject).getHealth() - 15);
        }
        if (subject instanceof Bullet) {
            if (((Bullet) subject).isPlayerBullet()) {
                setHit(true);
                setDirection(((Bullet) subject).getDirection());
                setDamageTaken(subject.getDamage());
                toDelete.add(subject);
            }
        }
        if (subject instanceof HomingMissile){
            if(((HomingMissile) subject).isPlayerMissile()){
                setHit(true);
                setDirection(((HomingMissile) subject).getDirection());
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
        PACIFIST, FIRING, SELF_DESTRUCT
    }

    private enum MovingState {
        DEFAULT_FORWARDS, BACKWARDS
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
            if (distanceBetween(this, target) <= 300f) {
                attackState = AttackState.FIRING;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

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
            case BACKWARDS:
                setPosition(new Vector2(getPosition().x - (float) Math.cos(getDirection()) * getSpeed() * delta,
                        getPosition().y - (float) Math.sin(getDirection()) * getSpeed() * delta)
                );
                setOrientation((float) (getDirection() + Math.PI));
                break;
        }
        switch (attackState) {
            case FIRING:
                int randomWeapon = MathUtils.random(1, 2);
                switch (randomWeapon) {
                    case 1:
                        missileAmmoCount = getMissileAmmoCount();
                        float missileShotCount = delta / EnemyMechanics.fireRate(20f) + missilesShotLeftOver;
                        int missileExactShotCount = Math.min(Math.round(missileShotCount), missileAmmoCount);

                        missileAmmoCount = missileAmmoCount - missileExactShotCount;
                        if (missileAmmoCount > 0) {
                            missilesShotLeftOver = missileShotCount - missileExactShotCount;
                        } else {
                            missilesShotLeftOver = 0;
                        }

                        for (int i = 0; i < missileExactShotCount; i++) {
                            toAdd.add(EnemyMechanics.loadMissile(this));
                        }
                        break;
                    case 2:
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
                    }
                    break;

            case SELF_DESTRUCT:
                toDelete.add(this);
                break;
            case PACIFIST:
                shotLeftOver = 0;
                missilesShotLeftOver = 0;
                break;
        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public int getMissileAmmoCount() {
        return missileAmmoCount;
    }
}


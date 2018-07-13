package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.BulletMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;

import java.util.Random;
import java.util.Set;

public class EnemyWithBullets extends Enemy {

    private int ammoCount;
    private float shotLeftOver;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;

    public EnemyWithBullets(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;


    }

    public enum AttackState {
        PACIFIST, FIRE_BULLETS, SELF_DESTRUCT
    }

    public enum MovingState {
        DEFAULT_FORWARDS, BACKWARDS, DODGING, FLY_AROUND, BLINK
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAggroDistance()) {
                float angle = angleBetween(this, target);
                float angleNoAim = angleBetweenNoAim(this, target);
                setOrientation(angle);
                setDirection(angleNoAim);
                attackState = AttackState.FIRE_BULLETS;
            }else {
                attackState = AttackState.PACIFIST;
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                attackState = AttackState.FIRE_BULLETS;
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

    public AttackState getAttackState() {
        return attackState;
    }

    public void setAttackState(AttackState attackState) {
        this.attackState = attackState;
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyChaser;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MinionMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;

import java.util.Random;
import java.util.Set;

public class EnemyMarkXMother extends Enemy {

    private int ammoCount;
    private float shotLeftOver;
    private int childrenAmmoCount;
    private float childrenShotLeftOver;
    private float time;
    private float changeDirectionTime;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_FORWARDS;

    /*
    Mark X Mother can fire Children and fly to the player when in range and when attacking. Aggro = 1000, Attack = 600
     */

    public EnemyMarkXMother(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        ammoCount = (int) Double.POSITIVE_INFINITY;
        shotLeftOver = ammoCount;
        childrenAmmoCount = 10;
        childrenShotLeftOver = childrenAmmoCount;

        changeDirectionTime = 30f;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Player){
            ((Player) subject).setHealth(((Player) subject).getHealth() - 200);
        }
        if (subject instanceof Bullet) {
            if (((Bullet) subject).isPlayerBullet()) {
                setHit(true);
                setDamageTaken(subject.getDamage());
                toDelete.add(subject);
                attackState = AttackState.FIRE_CHILDREN;
            }
        }
        if (subject instanceof HomingMissile){
            if(((HomingMissile) subject).isPlayerMissile()){
                setHit(true);
                setDamageTaken(subject.getDamage());
                toDelete.add(subject);
                attackState = AttackState.FIRE_CHILDREN;
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
        PACIFIST, FIRE_CHILDREN, FIRING, SELF_DESTRUCT
    }

    private enum MovingState {
        DEFAULT_FORWARDS, BACKWARDS
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= 1000f) {
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
            if (distanceBetween(this, target) <= 600f && distanceBetween(this, target) >= 400) {
                attackState = AttackState.FIRE_CHILDREN;
            }else if (distanceBetween(this, target) < 400){
                attackState = AttackState.FIRING;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
        if (target instanceof HomingMissile){
            if (((HomingMissile) target).isPlayerMissile()){
                float angle = angleBetween(this, target);
                setOrientation(angle);
                attackState = AttackState.FIRING;
            }else {
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
            case FIRE_CHILDREN:
                childrenAmmoCount = getChildrenAmmoCount();
                float childrenShotCount = delta / EnemyMechanics.fireRate(20f) + childrenShotLeftOver;

                int childrenExactShotCount = Math.min(Math.round(childrenShotCount), childrenAmmoCount);

                childrenAmmoCount = childrenAmmoCount - childrenExactShotCount;
                if (childrenAmmoCount > 0) {
                    childrenShotLeftOver = childrenShotCount - childrenExactShotCount;
                } else {
                    childrenShotLeftOver = 0;
                }
                for(int i = 0; i < childrenExactShotCount; i++) {
                    Random randomGenerator = new Random();
                    EnemyChaser enemyChaser = new EnemyChaser("ChaserMinion" + String.valueOf(childrenAmmoCount), new Vector2(
                            getPosition().x + randomGenerator.nextFloat() * getCollisionRadius(),
                            getPosition().y + randomGenerator.nextFloat() * getCollisionRadius()),
                            (int) (getMaxHealth()/10), getDirection(), getSpeed() * 6,
                            MinionMechanics.radius(1));
                    toAdd.add(enemyChaser);

                    float destructTime = 8.0f;
                    time += delta;
                    if(time >= destructTime){
                        float angle = angleBetween(this, enemyChaser);
                        enemyChaser.setPosition(new Vector2(this.getPosition().x +=  Math.cos(angle)  , this.getPosition().y +=  Math.sin(angle) ));
                        enemyChaser.setOrientation(angle);
                        enemyChaser.setDirection(angle);
                    }
                }
                break;
            case SELF_DESTRUCT:
                toDelete.add(this);
                break;
            case PACIFIST:
                shotLeftOver = 0;
                childrenShotLeftOver = 0;
                break;
        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }

    public int getChildrenAmmoCount() {
        return childrenAmmoCount;
    }
}

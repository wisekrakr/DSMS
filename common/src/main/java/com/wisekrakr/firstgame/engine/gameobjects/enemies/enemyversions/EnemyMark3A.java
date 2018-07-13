package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.MineMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;

import java.util.Set;

public class EnemyMark3A extends Enemy {

    private int minesCount;
    private float minesLeft;

    private float time;
    private float changeDirectionTime;

    private AttackState attackState = AttackState.PACIFIST;
    private MovingState movingState = MovingState.DEFAULT_BACKWARDS;

    /*
    Enemy Mark 3A fires mines when in range of a player and will always fly away from the player. Aggro = 300, Attack = 600
     */

    public EnemyMark3A(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);

        minesCount = (int) Double.POSITIVE_INFINITY;
        minesLeft = minesCount;
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
        PACIFIST, FIRE_MINES, SELF_DESTRUCT
    }

    private enum MovingState {
        DEFAULT_BACKWARDS, BACKWARDS
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= 300f) {
                float angle = angleBetween(this, target);
                float angleNoAim = angleBetweenNoAim(this, target);
                movingState = MovingState.DEFAULT_BACKWARDS;
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
            if (distanceBetween(this, target) <= 600f) {
                attackState = AttackState.FIRE_MINES;
                movingState = MovingState.BACKWARDS;
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
            case DEFAULT_BACKWARDS:
                time += delta;
                if (time >= changeDirectionTime) {
                    float randomDirection = EnemyMechanics.setRandomDirection();
                    setDirection(randomDirection);
                    time = 0;
                }
                setPosition(new Vector2(getPosition().x - (float) Math.cos(getDirection()) * getSpeed() * delta,
                        getPosition().y - (float) Math.sin(getDirection()) * getSpeed() * delta)
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
            case FIRE_MINES:
                minesCount = getMinesCount();
                float minesShotCount = delta / EnemyMechanics.fireRate(20f) + minesLeft;

                int exactMinesShotCount = Math.min(Math.round(minesShotCount), minesCount);

                minesCount = minesCount - exactMinesShotCount;
                if (minesCount > 0) {
                    minesLeft = minesShotCount - exactMinesShotCount;
                } else {
                    minesLeft = 0;
                }

                for (int i = 0; i < exactMinesShotCount; i++) {
                    toAdd.add(EnemyMechanics.loadMine(this));
                }
                break;
            case SELF_DESTRUCT:
                toDelete.add(this);
            case PACIFIST:
                minesLeft = 0;
        }
    }

    public int getMinesCount() {
        return minesCount;
    }
}

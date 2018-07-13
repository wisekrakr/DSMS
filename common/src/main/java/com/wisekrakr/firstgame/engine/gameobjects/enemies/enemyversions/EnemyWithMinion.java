package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyWeaponry;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;

import java.util.List;
import java.util.Set;

public class EnemyWithMinion extends Enemy {

    private Minion minionShooter;
    private float minionAngle;
    private boolean minionActivated = false;
    private float minionRotationSpeed;

    public EnemyWithMinion(String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(GameObjectType.EWM, name, position, health, direction, speed, radius);

        setAggroDistance(237.5f);
        setAttackDistance(187.5f);
        setChangeDirectionTime(3f);

        minionShooter = EnemyWeaponry.initMinion(this);
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.targetSpotted(target, toDelete, toAdd);
        setMovingState(MovingState.DEFAULT_FORWARDS);
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_BULLETS);
                setMovingState(MovingState.FLY_AROUND);
            }else{
                setAttackState(AttackState.PACIFIST);
                setMovingState(MovingState.DEFAULT_FORWARDS);
            }
        }
    }

    private void minionMovement(float delta){
        minionAngle += getMinionRotationSpeed() * delta;
        minionShooter.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * getSpeed() /3),
                (float) (getPosition().y + Math.sin(minionAngle) * getSpeed() /3)));
        if (minionActivated){
            minionShooter.setPosition(getPosition());
        }
        if (!(getHealth() <= getHealth()*(20f/100f))){
            setMinionRotationSpeed(getSpeed() / 10);
        }else {
            setMinionRotationSpeed(getSpeed() / 20);
        }
    }

    @Override
    public void afterAdd(List<GameObject> toAdd, List<GameObject> toRemove) {
        toAdd.add(minionShooter);
    }

    @Override
    public void afterRemove(List<GameObject> toAdd, List<GameObject> toRemove) {
        toAdd.add(minionShooter);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);
        minionMovement(delta);


        //toAdd.add(minionShooter);

    }

    public float getMinionAngle() {
        return minionAngle;
    }

    public boolean isMinionActivated() {
        return minionActivated;
    }

    public void setMinionActivated(boolean minionActivated) {
        this.minionActivated = minionActivated;
    }

    public float getMinionRotationSpeed() {
        return minionRotationSpeed;
    }

    public void setMinionRotationSpeed(float minionRotationSpeed) {
        this.minionRotationSpeed = minionRotationSpeed;
    }
}

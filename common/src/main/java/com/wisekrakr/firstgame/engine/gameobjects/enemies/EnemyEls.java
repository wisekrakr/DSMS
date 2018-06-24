package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;

import java.util.List;
import java.util.Set;

public class EnemyEls extends Enemy {

    private Minion minionShooter;

    public EnemyEls(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(GameObjectType.ELS, name, position, health, direction, speed, radius);

        setAggroDistance(237.5f);
        setAttackDistance(187.5f);
        setChangeDirectionTime(3f);

        minionShooter = initMinionShooter();
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

    @Override
    public void afterRemove(List<GameObject> toAdd, List<GameObject> toRemove) {
        toAdd.add(minionShooter);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);
        minionMovement(delta);
        if (!(getHealth() <= getHealth()*(20f/100f))){
            setMinionRotationSpeed(getSpeed() / 10);
        }else {
            setMinionRotationSpeed(getSpeed() / 20);
        }
        toAdd.add(minionShooter);

    }
}

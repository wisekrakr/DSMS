package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.Set;

public class EnemyHomer extends Enemy {

    public EnemyHomer(String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(GameObjectType.HOMER, name, position, health, direction, speed, radius);

        setAggroDistance(212.5f);
        setAttackDistance(150f);
        setChangeDirectionTime(12f);

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
                setAttackState(AttackState.FIRE_MISSILES);
                setMovingState(MovingState.FLY_AROUND);
            }else{
                setAttackState(AttackState.PACIFIST);
                setMovingState(MovingState.DEFAULT_FORWARDS);
            }
        }
    }
}

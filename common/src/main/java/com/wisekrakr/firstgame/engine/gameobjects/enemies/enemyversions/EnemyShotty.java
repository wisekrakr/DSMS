package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.Set;

public class EnemyShotty extends Enemy {

    public EnemyShotty(String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(GameObjectType.SHOTTY, name, position, health, direction, speed, radius);

        setAggroDistance(212.5f);
        setAttackDistance(200f);
        setChangeDirectionTime(17f);

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
                setMovingState(MovingState.DODGING);
                setAttackState(AttackState.FIRE_SHOTGUN);
            }else {
                setMovingState(MovingState.DEFAULT_FORWARDS);
                setAttackState(AttackState.PACIFIST);
            }
        }
    }
}


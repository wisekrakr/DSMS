package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.*;

public class EnemyChaser extends Enemy {

    public EnemyChaser(String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(GameObjectType.ENEMY_CHASER, name, position, health, direction, speed, radius);

        setAggroDistance(237.5f);
        setAttackDistance(187.5f);
        setChangeDirectionTime(5f);
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
                setMovingState(MovingState.DODGING);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.Set;

public class EnemyBlinker extends Enemy {
    private float time;

    public EnemyBlinker(String name, Vector2 position, double health, float direction, float speed, float radius) {
        super(GameObjectVisualizationType.BLINKER, name, position, health, direction, speed, radius);

        setAggroDistance(237.5f);
        setAttackDistance(187.5f);
        setChangeDirectionTime(8f);
    }


    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.targetSpotted(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (GameHelper.distanceBetween(this, target) <= getAggroDistance()  ) {
                setAttackState(AttackState.BLINK);
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (GameHelper.distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_LASER);
                setMovingState(MovingState.DODGING);
            }
        }
    }


    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);

        time += delta;
        if (getAttackState() == AttackState.PACIFIST) {
            if (time >= getChangeDirectionTime()) {
                setAttackState(AttackState.BLINK);
                time = 0;
            }
        }
    }
}


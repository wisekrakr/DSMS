package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.Set;

public class EnemyShitter extends Enemy {

    public EnemyShitter(String name, Vector2 position, double health, float direction, float speed, float radius) {
        super(GameObjectVisualizationType.SHITTER, name, position, health, direction, speed, radius);

        setAggroDistance(100);
        setAttackDistance(165);
        setChangeDirectionTime(16f);

    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

/*
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAggroDistance() ) {
                float angle = angleBetween(this, target);
                setPosition(new Vector2(getPosition().x -=  Math.cos(angle), getPosition().y -=  Math.sin(angle)));
                setOrientation(-angle);
                setDirection(-angle);
            }
        }
        */
        super.targetSpotted(target, toDelete, toAdd);
        setMovingState(MovingState.BACKWARDS);

    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (GameHelper.distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_MINES);
                setMovingState(MovingState.BACKWARDS);
            }else {
                setAttackState(AttackState.PACIFIST);
                setMovingState(MovingState.BACKWARDS);
            }
        }
    }

}
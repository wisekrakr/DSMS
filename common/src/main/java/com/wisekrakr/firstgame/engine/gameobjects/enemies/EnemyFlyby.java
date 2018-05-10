package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.Set;

public class EnemyFlyby extends Enemy {


    public EnemyFlyby(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);

        setAggroDistance(1500f);
        setAttackDistance(750f);
        setChangeDirectionTime(3f);
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAggroDistance()) {
                if (!(getHealth() <= getHealth()*(10f/100f))){
                    setMovingState(MovingState.FLY_BY);
                }else {
                    setMovingState(MovingState.BACKWARDS);
                    setAttackState(AttackState.PACIFIST);
                }
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                float angle = angleBetween(this, target);
                float angleNoAim = angleBetweenNoAim(this, target);
                setOrientation(angle);
                setDirection(angleNoAim);
                setMovingState(MovingState.DEFAULT_FORWARDS);
                setAttackState(AttackState.FIRE_BULLETS);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }


}
package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;

import java.util.Set;

public class EnemyMutator extends Enemy {

    public EnemyMutator(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(GameObjectType.MUTATOR, name, position, health, direction, speed, radius, space);

        setAggroDistance(250f);
        setAttackDistance(137.5f);
        setChangeDirectionTime(20f);

    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.targetSpotted(target, toDelete, toAdd);
        setMovingState(MovingState.DEFAULT_FORWARDS);
    }
/*
    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Bullet){
            setRadius(getRadius() - subject.getCollisionRadius());
            setCollisionRadius(getRadius());
            toDelete.add(subject);
        }
        if(subject instanceof HomingMissile){
            setRadius(getRadius() - subject.getCollisionRadius());
            setCollisionRadius(getRadius());
            toDelete.add(subject);
        }
        if(subject instanceof Player){
            toDelete.add(subject);
        }
    }
*/
    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_SPORES);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }

}

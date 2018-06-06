package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;

import java.util.Set;

public class EnemyFaceHugger extends Enemy {

    public EnemyFaceHugger(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(GameObjectType.FACE_HUGGER, name, position, health, direction, speed, radius, space);

        setAggroDistance(700f);
        setAttackDistance(300f);
        setChangeDirectionTime(3f);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Bullet || subject instanceof HomingMissile) {
            float angle = angleBetween(this, subject);
            setMovingState(MovingState.DEFAULT_FORWARDS);
            setOrientation(angle);
            setDirection(angle);
        }
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAggroDistance()) {
                if (!(getHealth() <= getHealth()*(10f/100f))){
                    float angle = angleBetween(this, target);
                    setMovingState(MovingState.DEFAULT_FORWARDS);
                    setOrientation(angle);
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
                setTargetVector(target.getPosition());
                setMovingState(MovingState.FACE_HUGGING);
                setAttackState(AttackState.FIRE_BULLETS);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }


}

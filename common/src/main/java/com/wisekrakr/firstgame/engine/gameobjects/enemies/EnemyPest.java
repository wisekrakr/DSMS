package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.HomingMissile;

import java.util.Set;

/**
 * EnemyPest is an Enemy that will clone a smaller version of itself when it gets hit with a missile. So kill it with something else!
 */

public class EnemyPest extends Enemy {

    public EnemyPest(String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(GameObjectType.PEST, name, position, health, direction, speed, radius);

        setAggroDistance(237.5f);
        setAttackDistance(187.5f);
        setChangeDirectionTime(3f);

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.collide(subject, toDelete, toAdd);

        if(subject instanceof HomingMissile){
            if (((HomingMissile) subject).isPlayerMissile()) {
                toAdd.add(new EnemyPest("pesty", this.getPosition(), 10, this.getOrientation(), this.getSpeed(),
                        getRadius() - ((HomingMissile) subject).getRadius()));
            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_BULLETS);
                setMovingState(MovingState.DEFAULT_FORWARDS);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.Set;

public class EnemyDodger extends Enemy {

    public EnemyDodger(String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(GameObjectVisualizationType.DODGER, name, position, health, direction, speed, radius);

        setAggroDistance(125f);
        setAttackDistance(175f);
        setChangeDirectionTime(20f);

    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.targetSpotted(target, toDelete, toAdd);
        setMovingState(MovingState.BACKWARDS);
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (GameHelper.distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_BULLETS);
                setMovingState(MovingState.DODGING);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }

}





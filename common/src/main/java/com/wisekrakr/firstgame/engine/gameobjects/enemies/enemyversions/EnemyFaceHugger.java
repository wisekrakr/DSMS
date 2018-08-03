package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.Set;

public class EnemyFaceHugger extends Enemy {

    public EnemyFaceHugger(String name, Vector2 position, double health, float direction, float speed, float radius) {
        super(GameObjectVisualizationType.FACE_HUGGER, name, position, health, direction, speed, radius);

        setAggroDistance(175f);
        setAttackDistance(75f);
        setChangeDirectionTime(3f);
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
            if (GameHelper.distanceBetween(this, target) <= getAttackDistance()) {
                setTargetVector(target.getPosition());
                setMovingState(MovingState.FACE_HUGGING);
                setAttackState(AttackState.FIRE_BULLETS);
            }else{
                setAttackState(AttackState.PACIFIST);
            }
        }
    }


}

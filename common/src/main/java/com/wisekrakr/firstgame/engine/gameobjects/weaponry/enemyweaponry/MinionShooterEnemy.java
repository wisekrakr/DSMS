package com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;

import java.util.Set;

public class MinionShooterEnemy extends Minion {

    public MinionShooterEnemy(String name, Vector2 position, int health, float direction, float radius,  SpaceEngine space) {
        super(GameObjectType.MINION_SHOOTER, name, position, health, direction, radius, space);

        setCollisionRadius(radius);
        setHealth(health);

        setAggroDistance(700f);
        setAttackDistance(600f);

        setEnemyMinion(true);

    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (target instanceof Player){
            if (distanceBetween(this, target)<= getAttackDistance()){
                if (!toDelete.contains(target)) {
                    setMinionAttackState(MinionAttackState.SHOOT);
                }else {
                    setMinionAttackState(MinionAttackState.PACIFIST);
                }
            }
        }
    }
    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);
    }

}

package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Minion;

import java.util.Set;

public class MinionFighterPlayer extends Minion {

    public MinionFighterPlayer(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(GameObjectType.MINION_FIGHTER, name, position, health, direction, radius, space);

        setCollisionRadius(radius);
        setHealth(health);

        setAggroDistance(900f);
        setAttackDistance(700f);

        setPlayerMinion(true);
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (target instanceof Enemy){
            if (distanceBetween(this, target)<= getAttackDistance()){
                if (!toDelete.contains(target)) {
                    setMinionAttackState(MinionAttackState.FIGHT);
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

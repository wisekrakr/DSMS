package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.BulletEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnemyShotty extends Enemy {

    public EnemyShotty(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);

        setAggroDistance(850);
        setAttackDistance(800);
        setChangeDirectionTime(17f);

    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (target instanceof Player) {

            if (distanceBetween(this, target) <= getAggroDistance()) {
                float angle = angleBetween(this, target);
                float angleNoAim = angleBetweenNoAim(this, target);

                setPosition(new Vector2(getPosition().x -= Math.cos(angle), getPosition().y -= Math.sin(angle)));
                setOrientation(angle);
                setDirection(angleNoAim);

            }
        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.attackTarget(target, toDelete, toAdd);
        if (target instanceof Player) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                setAttackState(AttackState.FIRE_SHOTGUN);
            }
        }
    }
}


package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MissileEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EnemyHomer extends Enemy {

    public EnemyHomer(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);

        setAggroDistance(850);
        setAttackDistance(600);
        setChangeDirectionTime(12f);

    }
}

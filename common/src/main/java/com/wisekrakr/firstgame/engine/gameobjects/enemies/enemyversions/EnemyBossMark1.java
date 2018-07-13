package com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;

import java.util.Set;

public class EnemyBossMark1 extends Enemy{

    public EnemyBossMark1(GameObjectType type, String name, Vector2 position, int health, float direction, float speed, float radius) {
        super(type, name, position, health, direction, speed, radius);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);
    }
}

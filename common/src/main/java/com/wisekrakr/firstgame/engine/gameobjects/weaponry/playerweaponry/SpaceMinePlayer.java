package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MinionShooterEnemy;

import java.util.Set;

public class SpaceMinePlayer extends SpaceMine {

    private int damage;
    private float radius;


    public SpaceMinePlayer(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(name, initialPosition, space, direction, speed, radius, damage);
        setDestructTime(20f);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Enemy){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
            setDestruct(true);
            initDebris(toDelete, toAdd);
        }
        if (subject instanceof MinionShooterEnemy){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
            setDestruct(true);
            initDebris(toDelete, toAdd);
        }
    }
}


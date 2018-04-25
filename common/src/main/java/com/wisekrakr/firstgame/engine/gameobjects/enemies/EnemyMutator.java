package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.BulletPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.MissilePlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Spores;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class EnemyMutator extends Enemy {

    public EnemyMutator(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, health, direction, speed, radius, space);

        setAggroDistance(1000);
        setAttackDistance(550);

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof BulletPlayer){
            setRadius(getRadius() - subject.getCollisionRadius());
            setCollisionRadius(getRadius());
            toDelete.add(subject);
        }
        if(subject instanceof MissilePlayer){
            setRadius(getRadius() - subject.getCollisionRadius());
            setCollisionRadius(getRadius());
            toDelete.add(subject);
        }
        if(subject instanceof Player){
            toDelete.add(subject);
        }
    }

}

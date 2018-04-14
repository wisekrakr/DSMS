package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;

import java.util.Set;

public class BulletPlayer extends Bullet {



    public BulletPlayer(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(name, initialPosition, space, direction, speed, radius, damage);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Enemy){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());

        }
    }


}

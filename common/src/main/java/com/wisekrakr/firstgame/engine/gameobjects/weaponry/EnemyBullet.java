package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.Set;


public class EnemyBullet extends Bullet {
    public EnemyBullet(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius) {
        super(name, initialPosition, space, direction, speed, radius);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - randomDamageCountBullet());
        }
    }
}

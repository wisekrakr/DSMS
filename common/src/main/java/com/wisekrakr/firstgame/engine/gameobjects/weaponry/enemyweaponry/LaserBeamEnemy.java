package com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.LaserBeam;

import java.util.Set;

public class LaserBeamEnemy extends LaserBeam {

    public LaserBeamEnemy(String name, Vector2 initialPosition, SpaceEngine space, float direction,  float radius, int damage) {
        super(name, initialPosition, space, direction,  radius, damage);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
        }
    }

}

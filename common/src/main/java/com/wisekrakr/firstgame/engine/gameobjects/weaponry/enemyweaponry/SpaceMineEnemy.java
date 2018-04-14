package com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.SpaceMine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SpaceMineEnemy extends SpaceMine {




    public SpaceMineEnemy(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(name, initialPosition, space, direction, speed, radius, damage);
        setDestructTime(10f);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Player){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());

            Random random = new Random();
            int debrisParts = random.nextInt(10)+1;
            for(int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 60, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getRadius()));

            }
        }
    }


}

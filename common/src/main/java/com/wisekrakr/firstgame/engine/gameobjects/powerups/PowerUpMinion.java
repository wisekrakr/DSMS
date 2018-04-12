package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.PowerUp;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BulletPlayer;

import java.util.Set;

public class PowerUpMinion extends PowerUp {

    public PowerUpMinion(String name, Vector2 initialPosition, SpaceEngine space) {
        super(name, initialPosition, space);

        setCollisionRadius(30);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }
}


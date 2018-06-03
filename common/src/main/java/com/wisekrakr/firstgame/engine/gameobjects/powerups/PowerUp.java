package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.Random;
import java.util.Set;

public class PowerUp extends GameObject {

    private float time;

    public PowerUp(GameObjectType type, String name, Vector2 initialPosition, SpaceEngine space) {
        super(type, name, initialPosition, space);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }
}

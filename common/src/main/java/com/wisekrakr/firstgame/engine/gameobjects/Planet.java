package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.Set;

public class Planet extends GameObject {

    public Planet(String name, Vector2 initialPosition, SpaceEngine space) {
        super(GameObjectVisualizationType.PLANET, name, initialPosition);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }
}

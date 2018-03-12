package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.Set;

public class VisionCone extends GameObject {

    public VisionCone(String name, Vector2 initialPosition, SpaceEngine space) {
        super(name, initialPosition, space);
    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }
}

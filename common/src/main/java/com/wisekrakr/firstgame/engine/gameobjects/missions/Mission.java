package com.wisekrakr.firstgame.engine.gameobjects.missions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.Set;

public class Mission extends GameObject {


    public Mission(GameObjectVisualizationType type, String name, Vector2 initialPosition) {
        super(type, name, initialPosition);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }

}

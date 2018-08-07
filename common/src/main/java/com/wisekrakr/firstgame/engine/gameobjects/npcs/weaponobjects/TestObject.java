package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;

import java.util.ArrayList;

public class TestObject extends WeaponObjectClass {

    private ArrayList<GameObject>fireWorks;

    public TestObject(GameObjectVisualizationType type, String name, Vector2 initialPosition, Behavior initialBehavior) {
        super(type, name, initialPosition, initialBehavior);
    }
}

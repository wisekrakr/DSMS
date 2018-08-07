package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WeaponObjectClass extends NonPlayerCharacter {

    public WeaponObjectClass(GameObjectVisualizationType type, String name, Vector2 initialPosition, Behavior initialBehavior) {
        super(type, name, initialPosition, initialBehavior);

    }

}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.Set;

public class BlankWeaponObject extends WeaponObjectClass {

    private GameObject weapon;

    public BlankWeaponObject(Vector2 initialPosition, Behavior initialBehavior, GameObject weapon, GameObject master) {
        super(master.getType(), "BlankWeapon", initialPosition, initialBehavior, master);
        this.weapon = weapon;
    }
}

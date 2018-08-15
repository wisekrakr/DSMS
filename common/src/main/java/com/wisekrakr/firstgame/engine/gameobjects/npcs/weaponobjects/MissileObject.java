package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.HomingMissileBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.Set;

public class MissileObject extends WeaponObjectClass{

    public MissileObject(Vector2 initialPosition, float initialDirection, double destructInterval, GameObject target, GameObject master) {
        super(GameObjectVisualizationType.MISSILE, "Misselito", initialPosition, master);

        this.rootBehavior(new HomingMissileBehavior(initialDirection, destructInterval, target));

        this.setCollisionRadius(3f);
        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));

    }


}

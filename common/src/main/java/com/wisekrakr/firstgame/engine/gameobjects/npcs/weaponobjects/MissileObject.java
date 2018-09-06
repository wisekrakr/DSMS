package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.HomingMissileBehavior;

public class MissileObject extends WeaponObjectClass{

    public MissileObject(Vector2 initialPosition, float initialDirection, GameObject target, GameObject master) {
        super(GameObjectVisualizationType.MISSILE, "Misselito", initialPosition, master);

        this.rootBehavior(new HomingMissileBehavior(initialDirection, target));

        this.setCollisionRadius(3f);
        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));

    }


}

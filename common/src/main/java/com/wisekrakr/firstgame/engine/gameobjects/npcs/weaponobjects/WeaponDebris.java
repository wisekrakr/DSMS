package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.SplashBehavior;


public class WeaponDebris extends WeaponObjectClass {

    private GameObject master;

    public WeaponDebris(Vector2 initialPosition, GameObject master) {
        super(GameObjectVisualizationType.DEBRIS, "debris", initialPosition, new SplashBehavior());
        this.master = master;
        setCollisionRadius(GameHelper.randomGenerator.nextFloat() * master.getCollisionRadius());
    }

}

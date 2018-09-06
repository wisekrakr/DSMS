package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.BulletBehavior;

public class BulletObject extends WeaponObjectClass {
    public BulletObject(Vector2 initialPosition, float initialDirection, GameObject master) {
        super(GameObjectVisualizationType.BULLET, "Bulletito", initialPosition, master);

        this.rootBehavior(new BulletBehavior());

        this.setDirection(initialDirection);
        this.setOrientation(initialDirection);
        this.setCollisionRadius(1f);
        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));
    }
}

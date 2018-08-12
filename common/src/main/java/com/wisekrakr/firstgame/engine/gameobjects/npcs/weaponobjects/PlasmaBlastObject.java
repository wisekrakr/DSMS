package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.TestBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.SplashBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.Set;

public class PlasmaBlastObject extends WeaponObjectClass {

    public PlasmaBlastObject(Vector2 initialPosition, float initialDirection, double destructInterval, GameObject master) {
        super(GameObjectVisualizationType.ASTEROID, "PlasmaBlast", initialPosition, new MyBehavior(initialPosition, initialDirection, destructInterval), master);

        setCollisionRadius(3f);
        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));
    }

    private static class MyBehavior extends Behavior {

        private final Vector2 initialPosition;
        private float initialDirection;
        private double destructInterval;

        public MyBehavior(Vector2 initialPosition, float initialDirection, double destructInterval) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
            this.destructInterval = destructInterval;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (!(context.existingSubBehavior() instanceof SplashBehavior)){
                context.pushSubBehavior(new SplashBehavior(initialDirection, destructInterval));
            }
        }
    }
}

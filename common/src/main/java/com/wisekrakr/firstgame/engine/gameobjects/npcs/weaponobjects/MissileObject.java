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

        rootBehavior(new MyBehavior(initialPosition, initialDirection, destructInterval, target));

        this.setCollisionRadius(3f);
        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));

    }

    private static class MyBehavior extends Behavior{

        private Vector2 initialPosition;
        private float initialDirection;
        private double destructInterval;
        private GameObject target;

        public MyBehavior(Vector2 initialPosition, float initialDirection, double destructInterval, GameObject target) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
            this.destructInterval = destructInterval;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (!(context.existingSubBehavior() instanceof HomingMissileBehavior)) {
                context.pushSubBehavior(new HomingMissileBehavior(initialDirection, destructInterval, target));
            }
        }
    }
}

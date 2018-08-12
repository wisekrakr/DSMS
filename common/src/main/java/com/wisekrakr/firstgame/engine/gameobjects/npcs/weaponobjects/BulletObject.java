package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.BulletBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.Set;

public class BulletObject extends WeaponObjectClass {
    public BulletObject(Vector2 initialPosition, float initialDirection, double destructInterval, GameObject master, float speed) {
        super(GameObjectVisualizationType.BULLET, "Bulletito", initialPosition, master);

        this.rootBehavior(new MyBehavior(destructInterval, speed));


        this.setDirection(initialDirection);
        this.setOrientation(initialDirection);
        this.setCollisionRadius(1f);
        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));
    }

    private class MyBehavior extends Behavior {
        private double destructInterval;
        private float speed;

        public MyBehavior(double destructInterval, float speed) {
            this.destructInterval = destructInterval;
            this.speed = speed;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {
            if (!(context.existingSubBehavior() instanceof BulletBehavior)) {
                if (getMaster() instanceof Spaceship) {
                    System.out.println("Starting at " + context.getDirection() + " master " + getMaster().getDirection() + " / " + getMaster().getOrientation());
                }
                context.pushSubBehavior(new BulletBehavior(destructInterval, speed));
            }
        }
    }
}

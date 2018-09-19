package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.TickTickBoomBehavior;

public class LaserObject extends WeaponObjectClass {

    public LaserObject(Vector2 initialPosition, float initialDirection,  double destructInterval, GameObject master) {
        super(GameObjectVisualizationType.LASER_BEAM, "Laserito", initialPosition, new MyBehavior(initialPosition, initialDirection, destructInterval), master);

        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));
    }

    private static class MyBehavior extends AbstractBehavior {

        private Vector2 initialPosition;
        private float initialDirection;
        private double destructInterval;

        public MyBehavior(Vector2 initialPosition, float initialDirection, double destructInterval) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
            this.destructInterval = destructInterval;
        }

        @Override
        public void elapseTime(float clock, float delta) {
            if (!(getContext().existingSubBehavior() instanceof TickTickBoomBehavior)){
                getContext().pushSubBehavior(new TickTickBoomBehavior(initialDirection, destructInterval));
            }
        }
    }
}

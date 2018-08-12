package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.BulletBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.TickTickBoomBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.Set;

public class LaserObject extends WeaponObjectClass {

    public LaserObject(Vector2 initialPosition, float initialDirection,  double destructInterval, GameObject master) {
        super(GameObjectVisualizationType.LASER_BEAM, "Laserito", initialPosition, new MyBehavior(initialPosition, initialDirection, destructInterval), master);

        this.setDamage(WeaponObjectMechanics.determineDamage(master, this));
    }

    private static class MyBehavior extends Behavior{

        private Vector2 initialPosition;
        private float initialDirection;
        private double destructInterval;

        public MyBehavior(Vector2 initialPosition, float initialDirection, double destructInterval) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
            this.destructInterval = destructInterval;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {
            if (!(context.existingSubBehavior() instanceof TickTickBoomBehavior)){
                context.pushSubBehavior(new TickTickBoomBehavior(initialDirection, destructInterval));
            }
        }
    }
}

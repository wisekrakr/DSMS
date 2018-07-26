package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.BulletBehavior;

public class BulletObject extends NonPlayerCharacter {

    public BulletObject(Vector2 initialPosition, float initialDirection) {
        super(GameObjectVisualizationType.BULLET, "Bulletito", initialPosition, new MyBehavior(initialPosition, initialDirection));

    }

    private static class MyBehavior extends Behavior {

        private final Vector2 initialPosition;
        private float initialDirection;


        public MyBehavior(Vector2 initialPosition, float initialDirection) {
            this.initialPosition = initialPosition;
            this.initialDirection = initialDirection;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (!(context.existingSubBehavior() instanceof BulletBehavior)){
                context.pushSubBehavior(new BulletBehavior(initialDirection));
            }
        }
    }
}

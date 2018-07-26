package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.server.ScenarioHelper;


public class TestNPC extends NonPlayerCharacter {

    public TestNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.TEST_NPC, "Chasey", initialPosition, new MyBehavior(initialPosition, null));
    }

    private static class MyBehavior extends Behavior {

        private final Vector2 initialPosition;
        private GameObject target;

        public MyBehavior(Vector2 initialPosition, GameObject target) {
            this.initialPosition = initialPosition;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            context.setActionDistance(600f);

            if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

            }else if (context.nearest() instanceof Player) {
                target = context.nearest();
                if (context.nearestInFloats() <= 200f) {
                    if (!(context.existingSubBehavior() instanceof BlinkingBehavior)) {
                        context.pushSubBehavior(new BlinkingBehavior());
                    }
                }
            }
        }
    }
}


/*
//Cruising -> Chasing & Shooting

            context.setActionDistance(500f);

            if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));
            }else if (context.nearest() instanceof Player){
                context.pushSubBehavior(new ChasingBehavior());
                if (context.nearestInFloats() <= 200f) {
                    context.pushSubBehavior(new ShootingBehavior(200, 0.5f, new BulletObject(context.getPosition(), context.getOrientation())));
                }
            }


//Cruising -> Chasing & HomingMissile
            context.setActionDistance(600f);

            if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

            }else if (context.nearest() instanceof Player) {
                target = context.nearest();
                context.pushSubBehavior(new ChasingBehavior());
                if (context.nearestInFloats() <= 300f) {
                    if (!(context.existingSubBehavior() instanceof ShootingBehavior)) {
                        context.pushSubBehavior(new ShootingBehavior(1, 0.5f, new MissileObject(context.getPosition(), context.getOrientation(), target)));
                    }
                }
            }

 */
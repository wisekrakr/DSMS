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
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.MissileObject;

public class AsteroidWatchingMissileShootingNPC extends NonPlayerCharacter{

    public AsteroidWatchingMissileShootingNPC(Vector2 initialPosition, float actionDistance) {
        super(GameObjectVisualizationType.BLINKER, "Missilier", initialPosition, new MyBehavior(initialPosition, actionDistance,null));
        this.setCollisionRadius(10f);
        this.setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));
        this.setActionDistance(actionDistance);
    }


    private static class MyBehavior extends Behavior {

        private final Vector2 initialPosition;
        private float actionDistance;
        private GameObject target;

        public MyBehavior(Vector2 initialPosition, float actionDistance, GameObject target) {
            this.initialPosition = initialPosition;
            this.actionDistance = actionDistance;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

            }else if (context.nearest() instanceof Player) {
                target = context.nearest();

                context.pushSubBehavior(new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                        new MissileObject(initialPosition, initialDirection, target, context.thisObject()), 2f, target));

            }else if (context.nearest() instanceof AsteroidNPC){
                target = context.nearest();
                if (!(context.existingSubBehavior() instanceof CirclingBehavior)) {
                    context.pushSubBehavior(new CirclingBehavior(target));
                }
            }
            if (context.getHealth() <= 0){
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }
        }
    }
}


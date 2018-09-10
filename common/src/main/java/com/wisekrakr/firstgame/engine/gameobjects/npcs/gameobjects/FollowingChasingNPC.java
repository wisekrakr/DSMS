package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class FollowingChasingNPC extends NonPlayerCharacter {

    public FollowingChasingNPC(Vector2 initialPosition, float actionDistance) {
        super(GameObjectVisualizationType.PEST, "Chasey", initialPosition, new MyBehavior(initialPosition, actionDistance, null));
        setCollisionRadius(14f);
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));
        setActionDistance(actionDistance);
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
            System.out.println(context.existingSubBehavior());
            if (context.getHealth() <= 0) {
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }else {
                if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                    context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 20f)));
                } else if (!(context.nearest() instanceof DebrisObject || !(context.nearest() instanceof WeaponObjectClass ||
                        !(context.nearest() instanceof FollowingChasingNPC)))) {
                    target = context.nearest();
                    context.pushSubBehavior(new ChasingBehavior(target));
                    if (context.nearestInFloats() <= actionDistance / 2) {
                        context.pushSubBehavior(new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                                new BulletObject(initialPosition, initialDirection, context.thisObject()), null, target));
                    }
                }
            }
        }
    }
}

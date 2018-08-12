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
            if (context.getHealth() <= 0) {
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            } else {
                if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                    context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 20f)));
                } else if (context.nearest() instanceof NonPlayerCharacter || context.nearest() instanceof Player) {

                    target = context.nearest();
                    context.pushSubBehavior(new ChasingBehavior(target));
                    if (context.nearestInFloats() <= actionDistance / 2) {
                        context.pushSubBehavior(new ShootingBehavior(new BulletObject(context.getPosition(), context.getOrientation(), 3,
                                context.thisObject(), 200), target));
                    }
                } else {
                    context.pushSubBehavior(new CruisingBehavior(8f));
                }

            }

        }
    }
}

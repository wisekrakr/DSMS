package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
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

    private static class MyBehavior extends AbstractBehavior {

        private final Vector2 initialPosition;
        private float actionDistance;
        private GameObject target;

        public MyBehavior(Vector2 initialPosition, float actionDistance, GameObject target) {
            this.initialPosition = initialPosition;
            this.actionDistance = actionDistance;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta) {

            if (getContext().getHealth() <= 0) {
                getContext().pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }else {
                if (!(getContext().existingSubBehavior() instanceof CruisingBehavior)) {
                    getContext().pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 20f)));
                } else if (!(getContext().nearest() instanceof DebrisObject || !(getContext().nearest() instanceof WeaponObjectClass ||
                        !(getContext().nearest() instanceof FollowingChasingNPC)))) {
                    target = getContext().nearest();
                    getContext().pushSubBehavior(new ChasingBehavior(target));
                    if (getContext().nearestInFloats() <= actionDistance / 2) {
                        getContext().pushSubBehavior(new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                                new BulletObject(initialPosition, initialDirection, getContext().thisObject()), null, target));
                    }
                } else if (getContext().nearest() instanceof WeaponObjectClass){
                    target = getContext().nearest();
                    if (getContext().collisionDetection(target)){
                        getContext().pushSubBehavior(new ReactiveBehavior(((WeaponObjectClass) target).getMaster()));
                    }
                }
            }
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.MissileObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class AsteroidWatchingMissileShootingNPC extends NonPlayerCharacter{

    public AsteroidWatchingMissileShootingNPC(Vector2 initialPosition, float actionDistance) {
        super(GameObjectVisualizationType.BLINKER, "Missilier", initialPosition, new MyBehavior(initialPosition, actionDistance,null));
        this.setCollisionRadius(10f);
        this.setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));
        this.setActionDistance(actionDistance);
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
            if (getContext().getHealth() <= 0){
                getContext().pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }else {
                if (!(getContext().existingSubBehavior() instanceof CruisingBehavior)) {
                    getContext().pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

                } else if (false) { // getContext().nearest() instanceof Player) {
                    target = getContext().nearest();

                    getContext().pushSubBehavior(new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                            new MissileObject(initialPosition, initialDirection, target, getContext().thisObject()), null, target));

                } else if (getContext().nearest() instanceof AsteroidNPC) {
                    target = getContext().nearest();
                    if (!(getContext().existingSubBehavior() instanceof CirclingBehavior)) {
                        getContext().pushSubBehavior(new CirclingBehavior(target));
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


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
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ExplodeAndLeaveDebrisBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.*;

import java.util.Set;


public class TestNPC extends NonPlayerCharacter {

    public TestNPC(Vector2 initialPosition, float actionDistance) {
        super(GameObjectVisualizationType.TEST_NPC, "TESTY", initialPosition, new MyBehavior(initialPosition, actionDistance, null));

        this.setCollisionRadius(GameHelper.generateRandomNumberBetween(8f, 15f));
        this.setActionDistance(actionDistance);
        this.setHealth(getCollisionRadius() * 3);
        //setDimensions(13, 31);
    }
/*
    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("width", getWidth());
        result.put("height", getHeight());

        return result;
    }
*/


    private static class MyBehavior extends AbstractBehavior{
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
                    getContext().pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 7f)));

                } else if (false) { // (getContext().nearest() instanceof Player) {
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


/*
//Cruising -> Chasing & Shooting

            getContext().setActionDistance(500f);

            if (!(getContext().existingSubBehavior() instanceof CruisingBehavior)) {
                getContext().pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));
            }else if (getContext().nearest() instanceof Player){
                getContext().pushSubBehavior(new ChasingBehavior());
                if (getContext().nearestInFloats() <= 200f) {
                    getContext().pushSubBehavior(new ShootingBehavior(200, 0.5f, new BulletObject(getContext().getPosition(), getContext().getOrientation())));
                }
            }


//Cruising -> Chasing & HomingMissile
            getContext().setActionDistance(600f);

            if (!(getContext().existingSubBehavior() instanceof CruisingBehavior)) {
                getContext().pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

            }else if (getContext().nearest() instanceof Player) {
                target = getContext().nearest();
                getContext().pushSubBehavior(new ChasingBehavior());
                if (getContext().nearestInFloats() <= 300f) {
                    if (!(getContext().existingSubBehavior() instanceof ShootingBehavior)) {
                        getContext().pushSubBehavior(new ShootingBehavior(1, 0.5f, new MissileObject(getContext().getPosition(), getContext().getOrientation(), target)));
                    }
                }
            }

 //Cruising -> shooting in different ways
            if (!(getContext().existingSubBehavior() instanceof CruisingBehavior)) {
                getContext().pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

            }else if (getContext().nearest() instanceof Player) {
                target = getContext().nearest();

                int shootingMood = (int) (clock % 6);
                switch (shootingMood) {
                    case 1:
                        getContext().pushSubBehavior(new ShootingBehavior(new BulletObject(
                                getContext().getPosition(), getContext().getOrientation(), 200, 0.5f)));
                        break;
                    case 4:
                        getContext().pushSubBehavior(new ShootingBehavior(new MissileObject(
                                getContext().getPosition(), getContext().getOrientation(), target)));
                        break;
                }
            }

 */
package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.PackageDeliveryMission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
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



    private static class MyBehavior extends Behavior{
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
                context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 7f)));

            }else if (context.nearest() instanceof Player) {
                target = context.nearest();

                context.pushSubBehavior(new ShootingBehavior((initialPosition, initialDirection, actionDistance) -> new PackageObject(initialPosition, context.thisObject()), target));

            }
            if (context.getHealth() <= 0){
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
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

 //Cruising -> shooting in different ways
            if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

            }else if (context.nearest() instanceof Player) {
                target = context.nearest();

                int shootingMood = (int) (clock % 6);
                switch (shootingMood) {
                    case 1:
                        context.pushSubBehavior(new ShootingBehavior(new BulletObject(
                                context.getPosition(), context.getOrientation(), 200, 0.5f)));
                        break;
                    case 4:
                        context.pushSubBehavior(new ShootingBehavior(new MissileObject(
                                context.getPosition(), context.getOrientation(), target)));
                        break;
                }
            }

 */
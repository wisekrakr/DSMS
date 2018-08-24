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

public class WearyTravellerFriendlyNPC extends NonPlayerCharacter {

    private Behavior desiredBehavior;

    public WearyTravellerFriendlyNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.SHOTTY, "Weary Space Traveller", initialPosition);

        setCollisionRadius(GameHelper.generateRandomNumberBetween(15f, 20f));
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3f));
        rootBehavior(new MyBehavior());
    }

    public void lookingForAHero() {
        desiredBehavior = new CruisingBehavior(4f);

    }

    public void chaseToGiveMission(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void fullStop(){
        desiredBehavior = new FullStopBehavior();
    }

    public void missionIsComplete() {
        desiredBehavior = new ExplodeAndLeaveDebrisBehavior(8f);
    }

    private class MyBehavior extends Behavior {
        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {
            if (desiredBehavior != null) {
                context.pushSubBehavior(desiredBehavior);
                desiredBehavior = null;
            }

            if (context.getHealth() <= 0){
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }
        }
    }
}

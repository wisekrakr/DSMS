package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;

public class Pervert extends NonPlayerCharacter {

    private Behavior desiredBehavior;

    public Pervert(Vector2 initialPosition) {
        super(GameObjectVisualizationType.EXHAUST, "Pervert", initialPosition);

        setCollisionRadius(6f);
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3f));

        rootBehavior(new MyBehavior());

    }

    public void lookingForADamsel(){
        desiredBehavior = new CruisingBehavior(10f);
    }

    public void chaseAfter(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void cirkelingDamsel(GameObject target){
        desiredBehavior = new CirclingBehavior(target);
    }

    private class MyBehavior extends Behavior{

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

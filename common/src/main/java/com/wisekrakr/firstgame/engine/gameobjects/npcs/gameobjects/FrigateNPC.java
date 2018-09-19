package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PackageObject;

public class FrigateNPC extends NonPlayerCharacter {

    private Behavior desiredBehavior;

    public FrigateNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.EWM, "Frigate", initialPosition);

        setCollisionRadius(GameHelper.generateRandomNumberBetween(30f, 45f));
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3f));
        rootBehavior(new MyBehavior());
    }

    public void cruising() {
        desiredBehavior = new CruisingBehavior(4f);
    }

    public void escaping(GameObject target) {
        desiredBehavior = new RunningEscapingBehavior(target);
    }

    public void fullStop(){
        desiredBehavior = new FullStopBehavior();
    }

    public void looseCargo(){
        desiredBehavior = new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                new PackageObject(initialPosition, this), 1f, null);
    }

    public void missionComplete() {
        desiredBehavior = new ExplodeAndLeaveDebrisBehavior(5f);
    }

    private class MyBehavior extends AbstractBehavior {
        @Override
        public void elapseTime(float clock, float delta) {
            if (getContext().getHealth() <= 0){
                getContext().pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }else {
                if (desiredBehavior != null) {
                    getContext().pushSubBehavior(desiredBehavior);
                    desiredBehavior = null;
                }
            }
        }
    }
}

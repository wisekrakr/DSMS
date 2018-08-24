package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Damsel extends NonPlayerCharacter{

    private Behavior desiredBehavior;
    private boolean clingingOn;

    public Damsel(Vector2 initialPosition) {
        super(GameObjectVisualizationType.DODGER, "Damsel", initialPosition);

        setCollisionRadius(GameHelper.generateRandomNumberBetween(5f, 10f));
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));

        rootBehavior(new MyBehavior());
    }

    public void runFrom(GameObject target) {
        desiredBehavior = new RunningEscapingBehavior(target);
    }

    public void clingOn(GameObject savior) {
        desiredBehavior = new StickyBehavior(savior);
        clingingOn = true;
    }

    public void missionComplete() {
        desiredBehavior = new ExplodeAndLeaveDebrisBehavior(13);
    }

    public void lookingForAHero() {
        desiredBehavior = new CruisingBehavior(4f);
    }

    public boolean isClingingOn() {
        return clingingOn;
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

package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.LaserObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PlasmaBlastObject;

import java.util.HashMap;
import java.util.Map;

public class BossMommaNPC extends NonPlayerCharacter {

    private Behavior desiredBehavior;

    public BossMommaNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.MOTHERSHIP, "Boss Momma", initialPosition);

        setCollisionRadius(100f);
        setHealth(20);

        rootBehavior(new MyBehavior());
    }

    public void announcePresence(){
        desiredBehavior = new CruisingBehavior(5f); //Change to something ominous.
    }

    public void aimFor(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void primaryAttack(GameObject target){

        desiredBehavior = new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                new PlasmaBlastObject(new Vector2(getPosition().x + getCollisionRadius() * (float)Math.cos(getOrientation()),
                        getPosition().y + getCollisionRadius() * (float)Math.sin(getOrientation())),
                        getOrientation(), 3f, this), 3f, target);

    }

    public void secondaryAttack(GameObject target){

        desiredBehavior = new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                new LaserObject(new Vector2(getPosition().x + getCollisionRadius() * (float)Math.cos(getOrientation()),
                        getPosition().y + getCollisionRadius() * (float)Math.sin(getOrientation())),
                        getOrientation(), 3f, this), 1f, target);

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

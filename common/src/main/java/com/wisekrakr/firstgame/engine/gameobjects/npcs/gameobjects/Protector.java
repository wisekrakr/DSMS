package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;

public class Protector extends NonPlayerCharacter {
    private Behavior desiredBehavior;

    public Protector(Vector2 initialPosition) {
        super(GameObjectVisualizationType.SHITTER, "Protector", initialPosition);

        setCollisionRadius(GameHelper.generateRandomNumberBetween(10f, 15f));
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));

        rootBehavior(new MyBehavior());
    }

    public void aimFor(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void shootAt(GameObject target){
        desiredBehavior = new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                new BulletObject(initialPosition, initialDirection, this), 0.8f, target);
    }

    public void protect(GameObject target) {
        desiredBehavior = new CirclingBehavior(target);
    }

    public void comeHome(GameObject target) {
        desiredBehavior = new StickyBehavior(target);
    }

    public void selfDestruct(){
        desiredBehavior = new ExplodeAndLeaveDebrisBehavior(13);
    }

    public void setDesiredBehavior(Behavior desiredBehavior) {
        this.desiredBehavior = desiredBehavior;
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

package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ExplodeAndLeaveDebrisBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ShootingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PlasmaBlastObject;

public class BossMommaNPC extends NonPlayerCharacter {

    private Behavior desiredBehavior;

    public BossMommaNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.MOTHERSHIP, "Boss Momma", initialPosition);

        setCollisionRadius(100f);
        setHealth(getCollisionRadius() * 3);

        rootBehavior(new MyBehavior());
    }


    public void aimFor(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void secondaryAttack(GameObject target){
        desiredBehavior = new ShootingBehavior((initialPosition, initialDirection, actionDistance) ->
                new PlasmaBlastObject(initialPosition, initialDirection, 2f, this), 3f, target);
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

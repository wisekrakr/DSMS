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

public class MutatorNPC extends NonPlayerCharacter {

    private Behavior desiredBehavior;

    public MutatorNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.MUTATOR, "Mutator", initialPosition);

        setCollisionRadius(75f);
        setHealth(getCollisionRadius() * 3);

        rootBehavior(new MyBehavior());
    }


    public void aimFor(GameObject target) {
        desiredBehavior = new ChasingBehavior(target);
    }

    public void attackWithSpores(GameObject target){
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

package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.RotatingBehavior;

public class Pervert extends NonPlayerCharacter {

    public Pervert(Vector2 initialPosition) {
        super(GameObjectVisualizationType.EXHAUST, "Pervert", initialPosition, new MyBehavior(null));

        setCollisionRadius(6f);
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3f));
        setActionDistance(300f);
    }

    private static class MyBehavior extends Behavior{

        private GameObject target;

        public MyBehavior(GameObject target) {
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (!(context.existingSubBehavior() instanceof CruisingBehavior)){
                context.pushSubBehavior(new CruisingBehavior(5f));
            }else if (context.nearest() instanceof Damsel){
                target = context.nearest();
                context.pushSubBehavior(new ChasingBehavior(target));
            }
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.IdleBehavior;

public class FollowingChasingNPC extends NonPlayerCharacter {


    public FollowingChasingNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.ENEMY_CHASER, "Chasey", initialPosition, new MyBehavior(initialPosition, null));
    }

    private static class MyBehavior extends Behavior {

        private final Vector2 initialPosition;
        private GameObject target;

        public MyBehavior(Vector2 initialPosition, GameObject target) {
            this.initialPosition = initialPosition;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            context.setActionDistance(350f);
            target = context.nearest();

            if (!(context.existingSubBehavior() instanceof IdleBehavior)){
                context.pushSubBehavior(new IdleBehavior());
            }else if (context.nearest() instanceof Player) {
                context.pushSubBehavior(new ChasingBehavior(target));
            }else{
                context.pushSubBehavior(new CruisingBehavior(8f));
            }

        }
    }
}

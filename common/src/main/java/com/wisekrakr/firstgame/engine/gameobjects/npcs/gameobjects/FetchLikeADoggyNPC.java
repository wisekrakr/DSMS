package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Bullet;

public class FetchLikeADoggyNPC extends NonPlayerCharacter{

    public FetchLikeADoggyNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.TEST_NPC, "Fetcher", initialPosition, new MyBehavior(initialPosition, null));
        setCollisionRadius(10f);

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

            context.setActionDistance(300f);

            if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                context.pushSubBehavior(new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 12f)));

            }else if (context.nearest() instanceof Player) {
                target = context.nearest();
                if (!(context.existingSubBehavior() instanceof ChasingBehavior)) {
                    context.pushSubBehavior(new ChasingBehavior());
                }
            }else if (context.nearest() instanceof Bullet){
                target = context.nearest();
                if (!(context.existingSubBehavior() instanceof ChasingBehavior)) {
                    context.pushSubBehavior(new ChasingBehavior());
                }
            }
        }
    }
}


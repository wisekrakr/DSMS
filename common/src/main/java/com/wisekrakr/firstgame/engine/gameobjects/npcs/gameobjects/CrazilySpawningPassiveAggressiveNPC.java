package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.MathUtils;
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
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.IdleBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.SpawningBehavior;
import com.wisekrakr.firstgame.server.ScenarioHelper;

import java.util.List;

public class CrazilySpawningPassiveAggressiveNPC extends NonPlayerCharacter {

    public CrazilySpawningPassiveAggressiveNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.MOTHERSHIP, "Passive aggressive", initialPosition, new MyBehavior(initialPosition, null));
        setCollisionRadius(20f);

    }

    private static class MyBehavior extends Behavior {
        private final Vector2 initialPosition;
        private int oldMood;
        private GameObject target;
        private float lastChange;

        public MyBehavior(Vector2 initialPosition, GameObject target) {
            this.initialPosition = initialPosition;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {


            lastChange += delta;

            if (lastChange >= 10f) {
                int mood = MathUtils.random(1, 3);
                switch (mood) {
                    case 1:
                        if (!(context.existingSubBehavior() instanceof CruisingBehavior)) {
                            context.pushSubBehavior(new CruisingBehavior(3f));
                        }

                        break;

                    case 2:
                        context.pushSubBehavior(new ChasingBehavior());

                        break;

                    case 3:
                        if (!(context.existingSubBehavior() instanceof SpawningBehavior)) {
                            if (context.nearest() instanceof Player) {
                                context.pushSubBehavior(new SpawningBehavior(ScenarioHelper.CHASER_FACTORY, 2f));
                            } else {
                                context.pushSubBehavior(new SpawningBehavior(ScenarioHelper.DODGER_FACTORY, 4f));
                            }
                        }

                        break;
                }
                lastChange = 0;
            }
        }
    }
}

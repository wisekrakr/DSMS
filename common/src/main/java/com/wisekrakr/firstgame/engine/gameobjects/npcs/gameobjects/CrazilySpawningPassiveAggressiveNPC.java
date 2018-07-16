package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.IdleBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.SpawningBehavior;
import com.wisekrakr.firstgame.server.ScenarioHelper;

import java.util.List;

public class CrazilySpawningPassiveAggressiveNPC extends NonPlayerCharacter {
    private List<GameObject> nearBy;

    public CrazilySpawningPassiveAggressiveNPC(Vector2 initialPosition) {
        super(GameObjectVisualizationType.BLINKER, "Passive aggressive", initialPosition, new MyBehavior(initialPosition, null));
    }

    private static class MyBehavior extends Behavior {
        private final Vector2 initialPosition;
        private int oldMood;
        private GameObject target;

        public MyBehavior(Vector2 initialPosition, GameObject target) {
            this.initialPosition = initialPosition;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {
            int mood = (int) (clock % 60);

            switch (mood) {
                case 0:
                    if (!(context.existingSubBehavior() instanceof IdleBehavior)) {
                        context.pushSubBehavior(new IdleBehavior());
                    }
                    break;

                case 5:
                    context.pushSubBehavior(new ChasingBehavior());

                    break;

                case 30:
                    if (!(context.existingSubBehavior() instanceof SpawningBehavior) || oldMood != mood) {
                        if (mood == 1) {
                            context.pushSubBehavior(new SpawningBehavior(ScenarioHelper.CHASER_FACTORY, 1f));
                        } else {
                            context.pushSubBehavior(new SpawningBehavior(ScenarioHelper.DODGER_FACTORY, 4f));
                        }
                    }

                    oldMood = mood;

                    break;
            }
        }
    }
}

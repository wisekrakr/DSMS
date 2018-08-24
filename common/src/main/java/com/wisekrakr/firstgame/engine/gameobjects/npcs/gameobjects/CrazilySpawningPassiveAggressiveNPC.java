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
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;
import com.wisekrakr.firstgame.server.ScenarioHelper;

import java.util.List;

public class CrazilySpawningPassiveAggressiveNPC extends NonPlayerCharacter {

    public CrazilySpawningPassiveAggressiveNPC(Vector2 initialPosition, float actionDistance) {
        super(GameObjectVisualizationType.MOTHERSHIP, "Passive aggressive NPC", initialPosition, new MyBehavior(initialPosition, actionDistance,  null));
        setCollisionRadius(20f);
        setHealth(GameHelper.generateRandomNumberBetween(getCollisionRadius(), getCollisionRadius() * 3));
        setActionDistance(actionDistance);
    }

    private static class MyBehavior extends Behavior {
        private final Vector2 initialPosition;
        private float actionDistance;
        private GameObject target;
        private float lastChange;

        public MyBehavior(Vector2 initialPosition, float actionDistance, GameObject target) {
            this.initialPosition = initialPosition;
            this.actionDistance = actionDistance;
            this.target = target;
        }

        @Override
        public void elapseTime(float clock, float delta, BehaviorContext context) {

            if (lastChange == 0){
                lastChange = clock;
            }

            
            if (clock - lastChange >= 20f) {
                int mood = MathUtils.random(1, 3);
                switch (mood) {
                    case 1:
                        if (!(context.existingSubBehavior() instanceof RotatingBehavior)) {
                            context.pushSubBehavior(new RotatingBehavior(25f));
                        }

                        break;

                    case 2:
                        if (!(context.nearest() instanceof WeaponObjectClass)) {
                            target = context.nearest();
                            context.pushSubBehavior(new ChasingBehavior(target));
                        }else {
                            context.pushSubBehavior(new CruisingBehavior(4f));
                        }

                        break;

                    case 3:
                        if (!(context.existingSubBehavior() instanceof SpawningBehavior)) {
                            int npcType = MathUtils.random(1, 2);
                            switch (npcType){
                                case 1:
                                    context.pushSubBehavior(new SpawningBehavior(ScenarioHelper.MISSILE_SHOOTER_FACTORY, 10f));
                                    break;
                                case 2:
                                    context.pushSubBehavior(new SpawningBehavior(ScenarioHelper.CHASING_SHOOTING_FACTORY, 10f));
                                    break;
                            }
                        }

                        break;
                }
                lastChange = clock;
            }

            if (context.getHealth() <= 0){
                context.pushSubBehavior(new ExplodeAndLeaveDebrisBehavior(8f));
            }
        }
    }
}

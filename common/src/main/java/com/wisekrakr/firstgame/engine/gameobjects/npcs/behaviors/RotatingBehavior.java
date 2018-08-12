package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;
import com.wisekrakr.firstgame.server.ScenarioHelper;

public class RotatingBehavior extends Behavior {

    private float rotatingSpeed;
    private float rotatingAngle;
    private boolean initialize = false;

    public RotatingBehavior(float rotatingSpeed) {
        this.rotatingSpeed = rotatingSpeed;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        rotatingAngle += rotatingSpeed * delta;
        context.setOrientation(rotatingAngle);

        if (!initialize) {
            context.setSpeed(GameHelper.generateRandomNumberBetween(1f, 100f));
            context.setDirection(GameHelper.randomDirection());

            initialize = true;
        }
    }
}

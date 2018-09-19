package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;
import com.wisekrakr.firstgame.server.ScenarioHelper;

public class RotatingBehavior extends AbstractBehavior {

    private float rotatingSpeed;
    private float rotatingAngle;
    private boolean initialize = false;

    public RotatingBehavior(float rotatingSpeed) {
        this.rotatingSpeed = rotatingSpeed;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        rotatingAngle += rotatingSpeed * delta;
        getContext().setOrientation(rotatingAngle);

        if (!initialize) {
            getContext().setSpeed(GameHelper.generateRandomNumberBetween(1f, 100f));
            getContext().setDirection(GameHelper.randomDirection());

            initialize = true;
        }
    }
}

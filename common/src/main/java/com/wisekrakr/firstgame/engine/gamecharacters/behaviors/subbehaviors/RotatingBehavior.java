package com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public class RotatingBehavior extends AbstractBehavior {
    private float rotatingSpeed;
    private float rotatingAngle;

    public RotatingBehavior(float rotatingSpeed) {
        this.rotatingSpeed = rotatingSpeed;
    }

    @Override
    public void start() {
        getContext().updatePhysicalObject(null, null, null, GameHelper.generateRandomNumberBetween(1f, 25f), GameHelper.randomDirection(), null, null);

        rotatingAngle = getContext().getSubject().getOrientation();
    }

    @Override
    public void elapseTime(float clock, float delta) {
        rotatingAngle += rotatingSpeed * delta;
        getContext().updatePhysicalObject(null, null, rotatingAngle, null, null, null, null);
    }
}

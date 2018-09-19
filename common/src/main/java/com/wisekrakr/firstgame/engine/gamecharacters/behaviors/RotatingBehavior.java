package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public class RotatingBehavior extends AbstractBehavior {
    private PhysicalObject subject;
    private float rotatingSpeed;
    private float rotatingAngle;

    public RotatingBehavior(PhysicalObject subject, float rotatingSpeed) {
        this.subject = subject;
        this.rotatingSpeed = rotatingSpeed;
    }

    @Override
    public void start() {
        getContext().updatePhysicalObject(subject, null, null, null, GameHelper.generateRandomNumberBetween(1f, 100f), GameHelper.randomDirection(), null, null);

        rotatingAngle = subject.getOrientation();
    }

    @Override
    public void elapseTime(float clock, float delta) {
        rotatingAngle += rotatingSpeed * delta;
        getContext().updatePhysicalObject(subject, null, null, rotatingAngle, null, null, null, null);
    }
}

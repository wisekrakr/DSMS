package com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public class TestBehavior extends AbstractBehavior {

    private float angle;

    public TestBehavior(float angle) {
        this.angle = angle;
    }

    @Override
    public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
        super.collide(object, epicentre, impact);
    }

    @Override
    public void elapseTime(float clock, float delta) {

        float updatedAngle = (float) (45f * Math.PI * delta);

        getContext().updatePhysicalObject(
                null,
                null,
                angle,
                null,
                angle + updatedAngle,
                null,
                null,
                null,
                null
        );

    }
}

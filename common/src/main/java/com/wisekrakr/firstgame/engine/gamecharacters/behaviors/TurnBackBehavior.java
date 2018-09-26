package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public class TurnBackBehavior extends AbstractBehavior {
    private final PhysicalObject target;
    private final float speedMagnitude;

    public TurnBackBehavior(PhysicalObject target, float speedMagnitude) {

        this.target = target;
        this.speedMagnitude = speedMagnitude;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null) {
            float angle = GameHelper.angleBetween(getContext().getSubject().getPosition(), target.getPosition());

            getContext().updatePhysicalObject(
                    null,
                    null,
                    -angle,
                    speedMagnitude,
                    -angle,
                    null,
                    null);
        }
    }


}

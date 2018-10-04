package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;

import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

import java.util.Arrays;
import java.util.List;

public class CruisingBehavior extends AbstractBehavior {
    private float changeDirectionInterval;
    private float speedMagnitude;
    private Float lastDirectionChange;

    public CruisingBehavior(float changeDirectionInterval, float speedMagnitude) {
        this.changeDirectionInterval = changeDirectionInterval;
        this.speedMagnitude = speedMagnitude;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (lastDirectionChange == null) {
            lastDirectionChange = clock;
        }

        if (clock - lastDirectionChange > changeDirectionInterval) {

            float randomDirection = GameHelper.randomDirection();
            getContext().updatePhysicalObject(
                    null, null,
                    randomDirection,
                    speedMagnitude,
                    randomDirection,
                    null, null);
            lastDirectionChange = clock;
        }
    }
}

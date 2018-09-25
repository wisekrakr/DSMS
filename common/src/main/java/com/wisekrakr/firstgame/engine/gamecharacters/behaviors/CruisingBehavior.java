package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;

import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

import java.util.Arrays;
import java.util.List;

public class CruisingBehavior extends AbstractBehavior {

    private PhysicalObject subject;
    private float changeDirectionInterval;
    private Float lastDirectionChange;

    public CruisingBehavior(PhysicalObject subject, float changeDirectionInterval) {
        this.subject = subject;
        this.changeDirectionInterval = changeDirectionInterval;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (lastDirectionChange == null) {
            lastDirectionChange = clock;
        }

        List<Float> speeds = Arrays.asList(GameHelper.generateRandomNumberBetween(30f, 35f), GameHelper.generateRandomNumberBetween(36f, 40f),
                GameHelper.generateRandomNumberBetween(41f, 45f));

        if (clock - lastDirectionChange > changeDirectionInterval) {

            float randomDirection = GameHelper.randomDirection();
            getContext().updatePhysicalObject(subject,
                    null, null,
                    randomDirection,
                    speeds.get(GameHelper.randomGenerator.nextInt(speeds.size())),
                    randomDirection,
                    null, null);
            lastDirectionChange = clock;
        }
    }
}

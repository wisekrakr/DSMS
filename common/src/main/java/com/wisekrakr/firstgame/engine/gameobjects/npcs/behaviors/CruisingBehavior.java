package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CruisingBehavior extends AbstractBehavior {

    private float changeDirectionInterval;
    private Float lastDirectionChange;

    public CruisingBehavior(float changeDirectionInterval) {
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
            getContext().setSpeed(speeds.get(GameHelper.randomGenerator.nextInt(speeds.size())));

            float randomDirection = GameHelper.randomDirection();
            getContext().setDirection(randomDirection);
            getContext().setOrientation(randomDirection);
            lastDirectionChange = clock;
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class IdleBehavior extends Behavior {
    private float changeDirectionInterval;
    private Float lastDirectionChange;

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        if (lastDirectionChange == null) {
            lastDirectionChange = clock;
        }

        if (clock - lastDirectionChange > changeDirectionInterval) {
            float randomDirection = EnemyMechanics.setRandomDirection();
            context.setOrientation(randomDirection);
            lastDirectionChange = clock;
        }
    }
}

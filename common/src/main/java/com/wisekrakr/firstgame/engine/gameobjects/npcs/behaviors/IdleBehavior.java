package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class IdleBehavior extends Behavior {

    private Float lastDirectionChange;

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (lastDirectionChange == null) {
            lastDirectionChange = clock;
        }

        if (clock - lastDirectionChange > 3f) {
            float randomDirection = EnemyMechanics.setRandomDirection();
            context.setDirection(randomDirection);
            context.setOrientation(randomDirection);
            lastDirectionChange = clock;
        }
        //context.setSpeed(50f);


    }

}
package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class IdleBehavior extends AbstractBehavior {

    private Float lastDirectionChange;

    @Override
    public void elapseTime(float clock, float delta) {

        if (lastDirectionChange == null) {
            lastDirectionChange = clock;
        }

        if (clock - lastDirectionChange > 3f) {
            float randomDirection = EnemyMechanics.setRandomDirection();
            getContext().setDirection(randomDirection);
            getContext().setOrientation(randomDirection);
            lastDirectionChange = clock;
        }
        //getContext().setSpeed(50f);


    }

}

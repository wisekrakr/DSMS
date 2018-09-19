package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

public class FullStopBehavior extends AbstractBehavior {


    @Override
    public void elapseTime(float clock, float delta) {
        getContext().setSpeed(0);
    }
}

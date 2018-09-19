package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

public class ExplodeAndLeaveDebrisBehavior extends AbstractBehavior {

    private boolean initialize = false;
    private float debrisParts;

    public ExplodeAndLeaveDebrisBehavior(float debrisParts) {
        this.debrisParts = debrisParts;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (getContext().thisObject() != null){
            getContext().removeGameObject(getContext().thisObject());
            if (!initialize) {
                for (int i = 0; i < debrisParts; i++) {
                    getContext().addGameObject(new DebrisObject(getContext().getPosition(), GameHelper.generateRandomNumberBetween(1f, getContext().getRadius()/2)));
                }
                initialize = true;
            }
        }
    }
}

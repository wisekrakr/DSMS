package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

public class ExplodeAndLeaveDebrisBehavior extends Behavior {

    private boolean initialize = false;
    private float debrisParts;

    public ExplodeAndLeaveDebrisBehavior(float debrisParts) {
        this.debrisParts = debrisParts;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (context.thisObject() != null){
            context.removeGameObject(context.thisObject());
            if (!initialize) {
                for (int i = 0; i < debrisParts; i++) {
                    context.addGameObject(new DebrisObject(context.getPosition(), GameHelper.generateRandomNumberBetween(1f, context.getRadius()/2)));
                }
                initialize = true;
            }
        }
    }
}

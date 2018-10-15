package com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.ArrayList;
import java.util.List;

public class ExplodeAndLeaveDebrisBehavior extends AbstractBehavior {
    private PhysicalObject subject;
    private int debrisParts;
    private float debrisMass;
    private float debrisAge;
    private List<PhysicalObject> bits = new ArrayList<>();

    public ExplodeAndLeaveDebrisBehavior(int debrisParts, float debrisMass, float debrisAge) {
        this.debrisParts = debrisParts;
        this.debrisMass = debrisMass;
        this.debrisAge = debrisAge;
    }

    @Override
    public void start() {
        for (int i = 0; i < debrisParts; i++) {
            /*
            bits.add(getContext().addPhysicalObject("debris",
                    subject.getPosition(),
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(1f, 4f),
                    GameHelper.randomDirection(),
                    Visualizations.BOULDER,
                    (float) Math.sqrt((debrisMass * debrisMass) / debrisParts),
                    null));
                    */
        }

        getContext().removePhysicalObject();
    }

    @Override
    public void elapseTime(float clock, float delta) {
        /*
        debrisAge = debrisAge - delta;
        if (debrisAge < 0) {
            for (PhysicalObject object: bits) {
                getContext().removePhysicalObject(object);
            }
        }
        */
    }
}

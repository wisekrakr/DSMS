package com.wisekrakr.firstgame.engine.scenarios;

import com.wisekrakr.firstgame.engine.SpaceEngine;

public class Scenario {
    private boolean swarmOn = false;

    public void periodicUpdate(SpaceEngine spaceEngine) {
    }

    public boolean isSwarmOn() {
        return swarmOn;
    }

    public void setSwarmOn(boolean swarmOn) {
        this.swarmOn = swarmOn;
    }
}

package com.wisekrakr.firstgame.engine;

import com.wisekrakr.firstgame.engine.scenarios.Scenario;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    private SpaceEngine space;
    private List<Scenario> scenarios = new ArrayList<>();
    private float previousUpdate = -1f;
    private float updateFrequence = 0.1f;

    public GameEngine(SpaceEngine space) {
        this.space = space;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    public void start() {
    }

    public void elapseTime(float f) {
        space.elapseTime(f);
        if (space.getTime() > previousUpdate + updateFrequence) {
            previousUpdate = space.getTime();

            periodicUpdate();
        }
    }

    private void periodicUpdate() {
        for(Scenario scenario: scenarios) {
            scenario.periodicUpdate(space);
        }
    }
}
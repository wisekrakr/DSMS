package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.server.EngineConstants;

import java.util.Random;

public class Scenario {
    private ScenarioContext context;

    protected final ScenarioContext getContext() {
        return context;
    }

    public final void init(ScenarioContext context) {
        this.context = context;
    }

    public void stop() {
    }

    public void destroy() {
    }

    public void start(){
    }

    public void periodicUpdate() {
    }


    public interface ScenarioContext {
        SpaceEngine space();
        GameEngine engine();
    }

}

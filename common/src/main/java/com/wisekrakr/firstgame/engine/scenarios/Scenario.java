package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.server.EngineConstants;

import java.util.Random;

public class Scenario {
    private Random randomGenerator = new Random();

    public void periodicUpdate(SpaceEngine spaceEngine) {
    }

    public boolean isSwarmOn() {

        return true;
    }


    Vector2 randomPosition() {
        return new Vector2(randomGenerator.nextFloat() * EngineConstants.ENGINE_WIDTH - EngineConstants.PLUS_XY,
                randomGenerator.nextFloat() * EngineConstants.ENGINE_HEIGHT - EngineConstants.PLUS_XY);
    }

    public float randomDirection(){
        return randomGenerator.nextFloat() * 2000 - 1000;
    }
}

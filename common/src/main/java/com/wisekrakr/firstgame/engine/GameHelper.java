package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.server.EngineConstants;

import java.util.Random;

public class GameHelper {
    /**
     * Utility methods available to scenarios
     */
    private static Random randomGenerator = new Random();

    public static Vector2 randomPosition() {
        return new Vector2(randomGenerator.nextFloat() * EngineConstants.ENGINE_WIDTH - EngineConstants.PLUS_XY,
                randomGenerator.nextFloat() * EngineConstants.ENGINE_HEIGHT - EngineConstants.PLUS_XY);
    }

    public static float randomDirection(){
        return randomGenerator.nextFloat() * 2000 - 1000;
    }
}

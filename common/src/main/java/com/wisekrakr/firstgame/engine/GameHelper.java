package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.server.EngineConstants;

import java.util.Random;

public class GameHelper {
    /**
     * Utility methods available to scenarios
     */
    public static Random randomGenerator = new Random();

    public static float generateRandomNumberBetween(float min, float max){
        return randomGenerator.nextFloat() * (max - min) + min;
    }

    public static Vector2 randomPosition() {
        return new Vector2(randomGenerator.nextFloat() * EngineConstants.ENGINE_WIDTH - EngineConstants.PLUS_XY,
                randomGenerator.nextFloat() * EngineConstants.ENGINE_HEIGHT - EngineConstants.PLUS_XY);
    }

    public static float randomDirection(){
        return randomGenerator.nextFloat() * 2000 - 1000;
    }


    public static float distanceBetween(GameObject subject, GameObject target) {
        return distanceBetween(subject.getPosition(), target.getPosition());
    }

    public static float distanceBetween(Vector2 subject, Vector2 target) {
        float attackDistanceX = target.x - subject.x;
        float attackDistanceY = target.y - subject.y;

        return (float) Math.hypot(attackDistanceX, attackDistanceY);
    }

    public static float angleBetween(GameObject subject, GameObject target) {
        return angleBetween(subject.getPosition(), target.getPosition());
    }

    public static float angleBetween(Vector2 subject, Vector2 target) {
        float attackDistanceX = target.x - subject.x;
        float attackDistanceY = target.y - subject.y;

        return (float) Math.atan2(attackDistanceY, attackDistanceX);
    }

    public static float healthBetween75And125(){
        return randomGenerator.nextInt(125) + 75;
    }

}

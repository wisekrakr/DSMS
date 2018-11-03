package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
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

    @Deprecated // TODO: move to the space engine
    public static Vector2 randomPosition() {
        return new Vector2(randomGenerator.nextFloat() * 10000 - 5000,
                randomGenerator.nextFloat() * 10000 - 5000);
    }

    public static float randomDirection(){
        return randomGenerator.nextFloat() * 2000 - 1000;
    }

    public static float distanceBetweenPhysicals(PhysicalObject subject, PhysicalObject target) {
        return distanceBetween(subject.getPosition(), target.getPosition());
    }



    public static float distanceBetween(Vector2 subject, Vector2 target) {
        float attackDistanceX = target.x - subject.x;
        float attackDistanceY = target.y - subject.y;

        return (float) Math.hypot(attackDistanceX, attackDistanceY);
    }


    public static float angleBetween(Vector2 subject, Vector2 target) {
        float attackDistanceX = target.x - subject.x;
        float attackDistanceY = target.y - subject.y;

        return (float) Math.atan2(attackDistanceY, attackDistanceX);
    }

    public static Vector2 applyMovement(Vector2 origin, float angle, float magnitude) {
        float speedX = (float) Math.cos(angle) * magnitude;
        float speedY = (float) Math.sin(angle) * magnitude;

        return new Vector2(origin.x + speedX, origin.y + speedY);
    }

}

package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import java.util.Random;

public class MineMechanics {

    public static int determineMineDamage(){
        Random random = new Random();
        return random.nextInt(50 - 10 + 1) + 10;
    }

    public static float radius(float multiplier){
        float radius = 2.0f;
        radius *= multiplier;
        return radius;
    }
}

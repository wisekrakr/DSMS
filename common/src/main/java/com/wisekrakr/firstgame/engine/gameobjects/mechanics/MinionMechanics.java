package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import java.util.Random;

public class MinionMechanics {
    public static int determineHealth(){
        Random random = new Random();
        return random.nextInt(50) + 30;
    }

    public static float radius(float multiplier){
        float radius = 2.5f;
        radius *= multiplier;
        return radius;
    }
}

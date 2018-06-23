package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import java.util.Random;

public class MissileMechanics {
    public static int determineMissileDamage(){
        Random random = new Random();
        return random.nextInt(20 - 10 + 1) + 10;
    }

    public static float radius(float multiplier){
        float radius = 1.25f;
        radius *= multiplier;
        return radius;
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import java.util.Random;

public class BulletMechanics {
    public static int determineBulletDamage(){
        Random random = new Random();
        return random.nextInt(10) + 1;
    }

    public static float radius(float multiplier){
        float radius = 0.8f;
        radius *= multiplier;
        return radius;
    }
}

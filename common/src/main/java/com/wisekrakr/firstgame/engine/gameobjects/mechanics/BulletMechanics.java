package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import java.util.Random;

public class BulletMechanics {
    public static int determineBulletDamage(){
        Random random = new Random();
        return random.nextInt(10) + 1;
    }
}

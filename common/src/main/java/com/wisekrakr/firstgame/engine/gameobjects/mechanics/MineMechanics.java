package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import java.util.Random;

public class MineMechanics {

    public static int determineMineDamage(){
        Random random = new Random();
        return random.nextInt(50 - 10 + 1) + 10;
    }
}

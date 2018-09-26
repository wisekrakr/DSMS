package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import java.util.Random;

public class EnemyMechanics {



    public static float setRandomDirection(){
        Random random = new Random();
        return random.nextFloat() * 2000 - 1000;
    }


}

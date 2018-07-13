package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;

import java.util.Random;

public class EnemyMechanics {



    public static float setRandomDirection(){
        Random random = new Random();
        return random.nextFloat() * 2000 - 1000;
    }


}

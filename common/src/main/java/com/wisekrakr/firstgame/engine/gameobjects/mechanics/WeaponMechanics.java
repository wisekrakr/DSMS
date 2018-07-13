package com.wisekrakr.firstgame.engine.gameobjects.mechanics;

public class WeaponMechanics {
    public static float fireRate(float multiplier){

        float rateOfFire = 0.1f;
        rateOfFire *= multiplier;

        return rateOfFire;
    }
}

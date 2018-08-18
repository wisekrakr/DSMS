package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;


import com.wisekrakr.firstgame.engine.gameobjects.GameObject;



public class WeaponObjectMechanics {

    public static int determineDamage(GameObject master, GameObject subject){
        float masterRadius = master.getCollisionRadius();
        float radius = subject.getCollisionRadius();

        if (radius == 1){
            radius = 1.2f;
        }

        return (int) (radius * (masterRadius / 3));
    }


}

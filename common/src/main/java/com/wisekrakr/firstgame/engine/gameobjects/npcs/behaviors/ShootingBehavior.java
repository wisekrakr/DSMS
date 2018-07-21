package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.CrazilySpawningPassiveAggressiveNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.FollowingChasingNPC;

public class ShootingBehavior extends Behavior {

    private int ammoCount;
    private float shotLeftOver;
    private float fireRate;
    private GameObject weapon;

    public ShootingBehavior(int ammoCount, float fireRate, GameObject weapon) {
        this.ammoCount = ammoCount;
        this.fireRate = fireRate;
        this.weapon = weapon;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        GameObject target = context.nearest();

        if (target != null){

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(context.getDirection());
            context.setOrientation(angle);
        }




/*
        ammoCount = getAmmoCount();
        shotLeftOver = ammoCount;
        float shotCount = delta / fireRate + shotLeftOver;
        int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

        ammoCount = ammoCount - exactShotCount;
        if (ammoCount > 0) {
            shotLeftOver = shotCount - exactShotCount;
        } else {
            shotLeftOver = 0;
        }
*/
        if (ammoCount != 0) {
            for (int i = 0; i < getAmmoCount(); i++) {
                context.addGameObject(weapon);
            }
        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }
}

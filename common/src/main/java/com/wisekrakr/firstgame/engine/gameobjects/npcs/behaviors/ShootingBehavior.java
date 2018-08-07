package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.SpaceMineObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class ShootingBehavior extends Behavior {
    private GameObject weapon;
    private GameObject target;

    public ShootingBehavior(GameObject weapon, GameObject target) {
        this.weapon = weapon;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (!(target instanceof WeaponObjectClass)) {

            float angle = GameHelper.angleBetween(context.getPosition(), context.nearest().getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);
            context.addGameObject(weapon);

        }



        //TODO: workout why its starts shooting faster when closer to player





    }
}

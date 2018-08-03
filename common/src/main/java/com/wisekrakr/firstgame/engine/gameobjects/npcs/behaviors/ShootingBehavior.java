package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.SpaceMineObject;

public class ShootingBehavior extends Behavior {
    private GameObject weapon;

    public ShootingBehavior(GameObject weapon) {
        this.weapon = weapon;

    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        GameObject target = context.nearest();

        if (target != null){

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);
        }
        //TODO: workout why its starts shooting faster when closer to player


        context.addGameObject(weapon);


    }
}

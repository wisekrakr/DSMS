package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class HomingMissileBehavior extends Behavior {
    private float initialDirection;
    private GameObject target;
    private float lastShot;

    public HomingMissileBehavior(float initialDirection, GameObject target) {

        this.initialDirection = initialDirection;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (!(target instanceof WeaponObjectClass) && !(target instanceof DebrisObject)  && target != null) {
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(130f);
        }else {
            context.setDirection(initialDirection);
            context.setOrientation(initialDirection);
            context.setSpeed(100f);
        }

        if (lastShot == 0){
            lastShot = clock;
        }


        if (clock - lastShot >= 8f) {
            context.removeGameObject(context.thisObject());
            lastShot = clock;
        }

    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class HomingMissileBehavior extends AbstractBehavior {
    private float initialDirection;
    private GameObject target;
    private float lastShot;

    public HomingMissileBehavior(float initialDirection, GameObject target) {

        this.initialDirection = initialDirection;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (!(target instanceof WeaponObjectClass) && !(target instanceof DebrisObject)  && target != null) {
            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setDirection(angle);
            getContext().setOrientation(angle);
            getContext().setSpeed(130f);
        }else {
            getContext().setDirection(initialDirection);
            getContext().setOrientation(initialDirection);
            getContext().setSpeed(100f);
        }

        if (lastShot == 0){
            lastShot = clock;
        }


        if (clock - lastShot >= 8f) {
            getContext().removeGameObject(getContext().thisObject());
            lastShot = clock;
        }

    }
}

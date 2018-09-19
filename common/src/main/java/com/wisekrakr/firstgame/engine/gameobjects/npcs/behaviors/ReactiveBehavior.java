package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class ReactiveBehavior extends AbstractBehavior {
    private GameObject target;

    public ReactiveBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null && !(target instanceof WeaponObjectClass)) {
            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setDirection(angle);
            getContext().setOrientation(angle);
            getContext().setSpeed(getContext().getSpeed());
        }
    }
}

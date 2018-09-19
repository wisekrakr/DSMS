package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.Set;

public class RunningEscapingBehavior extends AbstractBehavior {

    private GameObject target;

    public RunningEscapingBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null && !(target instanceof WeaponObjectClass)){
            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setSpeed(40f);
            getContext().setDirection(-angle);
            getContext().setOrientation(-angle);
        }
    }


}

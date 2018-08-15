package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class RunningEscapingBehavior extends Behavior {

    private GameObject target;

    public RunningEscapingBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null && !(target instanceof WeaponObjectClass)){
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setSpeed((Float) target.getExtraSnapshotProperties().get("speed") + 10f);
            context.setDirection(-angle);
            context.setOrientation(-angle);
        }
    }
}

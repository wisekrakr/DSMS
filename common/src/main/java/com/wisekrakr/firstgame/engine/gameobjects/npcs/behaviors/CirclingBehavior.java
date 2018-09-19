package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class CirclingBehavior extends AbstractBehavior{

    private GameObject target;

    public CirclingBehavior(GameObject target) {
        this.target = target;

    }

    @Override
    public void elapseTime(float clock, float delta) {

        float updatedAngle = (float) (45f * Math.PI * delta);

        if (target != null && !(target instanceof WeaponObjectClass)){

            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setSpeed(GameHelper.generateRandomNumberBetween(50f, 70f));
            getContext().setOrientation(angle);
            getContext().setDirection(angle + updatedAngle);
        }
    }
}

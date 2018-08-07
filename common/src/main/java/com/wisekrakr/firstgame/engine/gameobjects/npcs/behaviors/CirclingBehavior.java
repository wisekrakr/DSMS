package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

public class CirclingBehavior extends Behavior{

    private GameObject target;

    public CirclingBehavior(GameObject target) {
        this.target = target;

    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        float updatedAngle = (float) (45f * Math.PI * delta);

        if (target != null && !(target instanceof WeaponObjectClass)){

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setSpeed(GameHelper.generateRandomNumberBetween(50f, 70f));
            context.setOrientation(angle);
            context.setDirection(angle + updatedAngle);
        }
    }
}

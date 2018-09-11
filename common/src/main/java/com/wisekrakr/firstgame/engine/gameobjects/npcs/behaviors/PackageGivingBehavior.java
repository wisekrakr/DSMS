package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PackageObject;

public class PackageGivingBehavior extends Behavior {

    private boolean dropped;
    private GameObject target;

    public PackageGivingBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        float x = context.getPosition().x;
        float y = context.getPosition().y;

        float deltaX = (float) Math.cos(context.getOrientation());
        float deltaY = (float) Math.sin(context.getOrientation());
/*
        if (target != null) {
            if (!(dropped)) {
                context.addGameObject(new PackageObject(new Vector2(x + context.getRadius() * deltaX,
                        y + context.getRadius() * deltaY), context.thisObject()));
                dropped = true;
            }
        }
        */
    }

}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class CirclingBehavior extends Behavior{

    private Float lastDirectionChange;
    private float changeDirectionInterval;

    public CirclingBehavior(float changeDirectionInterval) {
        this.changeDirectionInterval = changeDirectionInterval;

    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        float updatedAngle = (float) (45f * Math.PI * delta);
        GameObject target = context.nearest();
        if (target != null){
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setOrientation(angle);
            context.setDirection(angle + updatedAngle);
        }
    }
}

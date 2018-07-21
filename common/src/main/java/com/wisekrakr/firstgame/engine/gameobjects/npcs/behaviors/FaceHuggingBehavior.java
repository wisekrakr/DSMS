package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class FaceHuggingBehavior extends Behavior {
    private float rotationAngle;

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        GameObject target = context.nearest();

        if (target != null){
            rotationAngle += 3f * delta;
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());
/*
            context.setPosition(new Vector2((float) (target.getPosition().x + Math.cos(rotationAngle) * 30f),
                    (float) (target.getPosition().y + Math.sin(rotationAngle) * 30f)));
*/
            context.setOrientation(angle);
            context.setDirection(angle);
            context.setSpeed(GameHelper.generateRandomNumberBetween(30f, 50f));
        }
    }
}

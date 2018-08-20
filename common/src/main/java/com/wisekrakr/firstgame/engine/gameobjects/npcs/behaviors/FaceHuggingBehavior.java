package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class FaceHuggingBehavior extends Behavior {
    private float rotationAngle;
    private GameObject target;
    private float updatedAngle;

    public FaceHuggingBehavior( float rotationAngle, GameObject target) {
        this.rotationAngle = rotationAngle;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null){

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());
            rotationAngle += 45f *  delta;

            context.setOrientation(angle );
            context.setDirection(angle);
            context.setSpeed((Float) target.getExtraSnapshotProperties().get("speed"));

            context.getPosition().x = (float) (target.getPosition().x + target.getCollisionRadius() + context.getRadius() +
                        Math.cos(rotationAngle) * delta);
            context.getPosition().y = (float) (target.getPosition().y + target.getCollisionRadius() + context.getRadius() +
                        Math.sin(rotationAngle) * delta);
        }
    }
}

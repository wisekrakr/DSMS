package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class FaceHuggingBehavior extends AbstractBehavior {
    private float rotationAngle;
    private GameObject target;
    private float updatedAngle;

    public FaceHuggingBehavior( float rotationAngle, GameObject target) {
        this.rotationAngle = rotationAngle;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null){

            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());
            rotationAngle += 45f *  delta;

            getContext().setOrientation(angle );
            getContext().setDirection(angle);
            getContext().setSpeed((Float) target.getExtraSnapshotProperties().get("speed"));

            getContext().getPosition().x = (float) (target.getPosition().x + target.getCollisionRadius() + getContext().getRadius() +
                        Math.cos(rotationAngle) * delta);
            getContext().getPosition().y = (float) (target.getPosition().y + target.getCollisionRadius() + getContext().getRadius() +
                        Math.sin(rotationAngle) * delta);
        }
    }
}

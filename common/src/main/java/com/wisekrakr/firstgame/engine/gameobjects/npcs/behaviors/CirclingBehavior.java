package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class CirclingBehavior extends Behavior{

    private float updatedAngle;
    private Float lastDirectionChange;
    private float changeDirectionInterval;
    private GameObject target;

    public CirclingBehavior(float changeDirectionInterval, GameObject target) {
        this.changeDirectionInterval = changeDirectionInterval;
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        target = context.nearest();

        if (lastDirectionChange == null){
            lastDirectionChange = clock;
        }

        if (clock - lastDirectionChange > changeDirectionInterval) {
            updatedAngle = (float) (45 * Math.PI * delta);
            lastDirectionChange = clock;
        }
        float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

        context.setOrientation(angle);
        context.setDirection(angle + updatedAngle);
        context.setSpeed(GameHelper.generateRandomNumberBetween(60f, 90f));

    }


}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;

public class TestBehavior extends Behavior {

    private NonPlayerCharacter weapon;

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        GameObject target = context.nearest();

        if (target != null) {
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());
            context.setOrientation(angle);
            context.setDirection(angle);
            context.setSpeed(100f);

            if (context.collisionDetection(target)) {



            }
        }
    }

}

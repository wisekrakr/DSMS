package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class ChasingBehavior extends Behavior {
    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        GameObject target = context.nearest();
        if (target != null) {
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());
            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(GameHelper.generateRandomNumberBetween(70f, 80f));
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;


import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class ChasingBehavior extends Behavior {

    private GameObject target;

    public ChasingBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null) {
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());
            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(GameHelper.generateRandomNumberBetween(70f, 80f));
        }
    }
}

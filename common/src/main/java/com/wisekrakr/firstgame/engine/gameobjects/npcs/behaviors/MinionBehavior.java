package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;



public class MinionBehavior extends Behavior {
    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        GameObject target = context.nearest();

        if (target != null){
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setDirection(angle);
            context.setOrientation(target.getPosition().angle());
            context.setSpeed(50);
            if (context.collisionDetection(target)){
                context.getPosition().x = (float) (target.getPosition().x + target.getCollisionRadius() + Math.sin(angle + 45f * delta));
                context.getPosition().y = (float) (target.getPosition().y + target.getCollisionRadius() + Math.cos(angle + 45f * delta));
            }

        }
    }
}

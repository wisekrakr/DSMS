package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class StickyBehavior extends Behavior {


    //TODO: If an object of the same class sticks, it will freak out. Also, a train  must be formed....but how
    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        GameObject target = context.nearest();
        if (target != null) {

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());
            context.setOrientation(angle);
            context.setDirection(angle);
            context.setSpeed(100f);


            if (context.collisionDetection(target)) {
                if (target.getClass() == context.getClass()){
                    context.removeGameObject(target);
                }else {
                    context.getPosition().x = target.getPosition().x + target.getCollisionRadius() * (float) Math.cos(target.getOrientation() + Math.PI);
                    context.getPosition().y = target.getPosition().y + target.getCollisionRadius() * (float) Math.sin(target.getOrientation() + Math.PI);

                    context.setSpeed((Float) target.getExtraSnapshotProperties().get("speed"));
                }
            }
        }
    }
}

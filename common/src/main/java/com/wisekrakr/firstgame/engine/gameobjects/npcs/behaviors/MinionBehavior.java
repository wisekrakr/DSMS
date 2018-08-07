package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;



public class MinionBehavior extends Behavior {

    private GameObject target;

    public MinionBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {


        if (target != null && !(target.getClass() == context.thisObject().getClass())){
            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());



            context.setDirection(angle);
            context.setOrientation(angle);
            context.setSpeed(50);
            if (context.collisionDetection(target)){
                //context.getPosition().setAngle(angle + rotation);
                angle += 45f * delta;
                context.getPosition().x += (float) (target.getPosition().x + target.getCollisionRadius() *2 + Math.cos(angle) * 80f);
                context.getPosition().y += (float) (target.getPosition().y + target.getCollisionRadius() *2 + Math.sin(angle) * 80f);

            }

        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class StickyBehavior extends Behavior {

    private GameObject target;

    public StickyBehavior(GameObject target) {
        this.target = target;
    }

    //TODO: If an object of the same class sticks, it will freak out. Also, a train  must be formed....but how
    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {

        if (target != null && !(target.getClass() == context.thisObject().getClass())) {
            context.setSpeed((Float) target.getExtraSnapshotProperties().get("speed"));

            float angle = GameHelper.angleBetween(context.getPosition(), target.getPosition());

            context.setOrientation(angle);
            context.setDirection(angle);

            if (context.collisionDetection(target)) {

                context.getPosition().x = (target.getPosition().x + target.getCollisionRadius() + context.getRadius());
                context.getPosition().y = (target.getPosition().y + target.getCollisionRadius() + context.getRadius());
                context.setOrientation(target.getOrientation());
            }
        }
    }
}

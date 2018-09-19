package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

public class StickyBehavior extends AbstractBehavior {

    private GameObject target;

    public StickyBehavior(GameObject target) {
        this.target = target;
    }

    //TODO: If an object of the same class sticks, it will freak out. Also, a train  must be formed....but how
    @Override
    public void elapseTime(float clock, float delta) {

        if (target != null && !(target.getClass() == getContext().thisObject().getClass())) {
            getContext().setSpeed((Float) target.getExtraSnapshotProperties().get("speed"));

            float angle = GameHelper.angleBetween(getContext().getPosition(), target.getPosition());

            getContext().setOrientation(angle);
            getContext().setDirection(angle);

            if (getContext().collisionDetection(target)) {

                getContext().getPosition().x = (target.getPosition().x + target.getCollisionRadius() + getContext().getRadius());
                getContext().getPosition().y = (target.getPosition().y + target.getCollisionRadius() + getContext().getRadius());
                getContext().setOrientation(target.getOrientation());
            }
        }
    }
}

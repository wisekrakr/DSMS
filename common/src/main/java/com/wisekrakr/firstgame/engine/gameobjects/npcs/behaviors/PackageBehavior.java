package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;

public class PackageBehavior extends AbstractBehavior {

    private boolean dropped;
    private GameObject target;

    public PackageBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta) {
        float x = target.getPosition().x;
        float y = target.getPosition().y;

        float deltaX = (float) Math.cos(-target.getOrientation());
        float deltaY = (float) Math.sin(-target.getOrientation());

        getContext().getPosition().x = (x + target.getCollisionRadius() + getContext().getRadius() * deltaX);
        getContext().getPosition().y = (y + target.getCollisionRadius() + getContext().getRadius() * deltaY);

    }

}

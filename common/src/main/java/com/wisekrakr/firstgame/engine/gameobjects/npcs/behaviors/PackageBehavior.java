package com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PackageObject;

public class PackageBehavior extends Behavior {

    private boolean dropped;
    private GameObject target;

    public PackageBehavior(GameObject target) {
        this.target = target;
    }

    @Override
    public void elapseTime(float clock, float delta, BehaviorContext context) {
        float x = target.getPosition().x;
        float y = target.getPosition().y;

        float deltaX = (float) Math.cos(-target.getOrientation());
        float deltaY = (float) Math.sin(-target.getOrientation());

        context.getPosition().x = (x + target.getCollisionRadius() + context.getRadius() * deltaX);
        context.getPosition().y = (y + target.getCollisionRadius() + context.getRadius() * deltaY);

    }

}

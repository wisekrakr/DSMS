package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public class AbstractBehavior implements Behavior {
    private BehaviorContext context;

    @Override
    public final void init(BehaviorContext context) {
        this.context = context;
    }

    public BehaviorContext getContext() {
        return context;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void elapseTime(float clock, float delta) {
    }

    @Override
    public void collide(PhysicalObject object, Vector2 epicentre, float impact) {

    }
}

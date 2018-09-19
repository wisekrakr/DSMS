package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

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
}

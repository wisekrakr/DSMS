package com.wisekrakr.firstgame.engine.gamecharacters;

public class AbstractGameCharacter implements GameCharacter {
    private GameCharacterContext context;

    @Override
    public final void init(GameCharacterContext context) {
        this.context = context;
    }

    protected final GameCharacterContext getContext() {
        return context;
    }

    @Override
    public void start() {
    }

    @Override
    public void elapseTime(float delta) {
    }

    @Override
    public void stop() {
    }
}

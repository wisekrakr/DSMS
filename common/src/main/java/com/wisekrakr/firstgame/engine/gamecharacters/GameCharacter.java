package com.wisekrakr.firstgame.engine.gamecharacters;




public interface GameCharacter {
    void init(GameCharacterContext context);

    void start();

    void elapseTime(float delta);

    void stop();
}

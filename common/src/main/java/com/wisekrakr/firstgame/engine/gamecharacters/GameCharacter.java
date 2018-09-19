package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;

import java.util.*;

public interface GameCharacter {
    void init(GameCharacterContext context);

    void start();

    /**
     * update the state taking into account an elapsed time of delta seconds
     */
    void elapseTime(float delta);

    void stop();
}

package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

public interface GameObjectFactory<GameObjectT extends GameObject> {
    GameObjectT create(Vector2 initialPosition, float initialDirection, float actionDistance);


}

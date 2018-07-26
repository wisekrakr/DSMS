package com.wisekrakr.firstgame.engine.gameobjects.npcs;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;


import java.util.List;

public interface BehaviorContext {
    void pushSubBehavior(Behavior b);
    Behavior existingSubBehavior();

    void addGameObject(GameObject newObject);
    void removeGameObject(GameObject object);

    List<GameObject> nearby();

    GameObject nearest();

    float nearestInFloats();

    GameObject thisGameObject();

    Vector2 getPosition();

    float getSpeed();
    float getOrientation();
    float getDirection();
    float getHealth();
    float actionDistance();

    void setOrientation(float orientation);
    void setDirection(float direction);
    void setSpeed(float speed);
    void setHealth(float health);
    void setActionDistance(float actionDistance);

}

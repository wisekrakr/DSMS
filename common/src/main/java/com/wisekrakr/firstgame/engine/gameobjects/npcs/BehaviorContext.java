package com.wisekrakr.firstgame.engine.gameobjects.npcs;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;


import java.util.List;

public interface BehaviorContext {
    void pushSubBehavior(Behavior b);
    Behavior existingSubBehavior();

    Vector2 getPosition();

    float getSpeed();
    float getOrientation();
    float getDirection();
    double getHealth();
    //TODO: change to width/height dimensions, to make different kind of shapes later on (for collision detection)
    float getRadius();
    float getActionDistance();

    void setOrientation(float orientation);
    void setDirection(float direction);
    void setSpeed(float speed);
    void setHealth(double health);
    void setRadius(float radius);
    void setActionDistance(float actionDistance);

}

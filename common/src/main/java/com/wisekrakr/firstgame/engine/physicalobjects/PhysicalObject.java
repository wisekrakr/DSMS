package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public interface PhysicalObject {
    String getName();

    Vector2 getPosition();
    float getOrientation();
    float getSpeedMagnitude();
    float getSpeedDirection();
    Visualizations getVisualization();
    Map<String, Object> getExtraProperties();

    // TODO: for now this is our shape model. We will replace this later with something more complex, using the box2d engine
    float getCollisionRadius();
}

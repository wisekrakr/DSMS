package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.Map;
import java.util.Set;

public interface PhysicalObject {
    Set<String> getTags();
    String getName();

    Vector2 getPosition();
    float getOrientation();
    float getSpeedMagnitude();
    float getSpeedDirection();

    Visualizations getVisualization();
    Map<String, Object> getExtraProperties();

    // TODO: for now this is our shape model. We will replace this later with something more complex
    float getCollisionRadius();
}

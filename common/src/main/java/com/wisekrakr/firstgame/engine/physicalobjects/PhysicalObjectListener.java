package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

public interface PhysicalObjectListener {
    void collision(PhysicalObject two, float time, Vector2 epicentre, float impact);
}

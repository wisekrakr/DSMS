package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

public class AbstractPhysicalObjectListener implements PhysicalObjectListener {
    @Override
    public void collision(PhysicalObject myself, PhysicalObject two, float time, Vector2 epicentre, float impact) {

    }

    @Override
    public void removed(PhysicalObject target) {

    }
}

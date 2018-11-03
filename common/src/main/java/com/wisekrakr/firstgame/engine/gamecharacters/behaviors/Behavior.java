package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public interface Behavior {
    void init(BehaviorContext context);

    void start();

    void stop();

    // TODO: ditch clock?
    void elapseTime(float clock, float delta);

    void collide(PhysicalObject object, Vector2 epicentre, float impact);


}
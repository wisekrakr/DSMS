package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

public interface PhysicalObjectFactory<PhysicalObjectT extends PhysicalObject> {

    PhysicalObjectT create(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener alistener);


}

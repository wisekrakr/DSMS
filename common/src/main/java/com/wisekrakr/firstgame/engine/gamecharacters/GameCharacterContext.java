package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.List;

public interface GameCharacterContext {
    SpaceEngine getSpaceEngine();

    /**
     * Create a character that will live independently from this character
     */

    void addCharacter(GameCharacter newObject);

    PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, float health, float damage, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener alistener);
    void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Float health, Float damage, Visualizations visualizationEngine, Float collisionRadius);
    void updatePhysicalObjectExtra(PhysicalObject target, String key, Object value);
    void removePhysicalObject(PhysicalObject object);

    void removeMyself();

    PhysicalObject getPhysicalObject();
    List<NearPhysicalObject> findNearbyPhysicalObjects(PhysicalObject reference, float radius);
}

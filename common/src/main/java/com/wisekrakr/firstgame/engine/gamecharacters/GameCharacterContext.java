package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.physicalobjects.*;

import java.util.List;

public interface GameCharacterContext {
    SpaceEngine getSpaceEngine();

    /**
     * Create a character that will live independently from this character
     */
    void addCharacter(GameCharacter newObject, GameCharacterListener listener);

    PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener alistener, PhysicalObjectEvictionPolicy evictionPolicy);
    void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection,Visualizations visualizationEngine, Float collisionRadius);
    void updatePhysicalObjectExtra(PhysicalObject target, String key, Object value);
    void tagPhysicalObject(PhysicalObject target, String tag);
    void untagPhysicalObject(PhysicalObject target, String tag);
    void removePhysicalObject(PhysicalObject object);

    void removeMyself();

    PhysicalObject getPhysicalObject();
    List<NearPhysicalObject> findNearbyPhysicalObjects(PhysicalObject reference, float radius);
}

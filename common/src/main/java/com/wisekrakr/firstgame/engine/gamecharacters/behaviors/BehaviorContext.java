package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

public interface BehaviorContext {
//    void pushSubBehavior(Behavior b);

//    Behavior existingSubBehavior();

    void addCharacter(GameCharacter newObject);

//    PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualization, float collisionRadius, PhysicalObjectListener alistener);

    void updatePhysicalObject(String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Float health, Float damage, Visualizations visualization, Float collisionRadius);
    void updatePhysicalObjectExtra(String key, Object value);
    void removePhysicalObject();

    PhysicalObject getSubject();

/*

    List<GameObject> nearby();

    GameObject nearest();

    float nearestInFloats();

    GameObject thisObject();

    boolean collisionDetection(GameObject object);

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
*/
}

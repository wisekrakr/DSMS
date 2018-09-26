package com.wisekrakr.firstgame.engine.physicalobjects;

public class NearPhysicalObject {
    private PhysicalObject object;
    private float distance;

    public NearPhysicalObject(PhysicalObject object, float distance) {
        this.object = object;
        this.distance = distance;
    }

    public PhysicalObject getObject() {
        return object;
    }

    public float getDistance() {
        return distance;
    }
}

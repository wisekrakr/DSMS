package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

public class NearestPhysicalObject {
    private PhysicalObject one;
    private PhysicalObject two;
    private float time;


    //TODO: Two,  maybe needs to be a list of PhysicalObjects ===> more objects can hit another object at the same time?
    public NearestPhysicalObject(PhysicalObject one, PhysicalObject two, float time) {
        this.one = one;
        this.two = two;
        this.time = time;

    }

    public PhysicalObject getOne() {
        return one;
    }

    public PhysicalObject getTwo() {
        return two;
    }

    public float getTime() {
        return time;
    }


}

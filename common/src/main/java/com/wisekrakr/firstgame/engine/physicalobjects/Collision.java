package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

public class Collision {
    private PhysicalObject one;
    private PhysicalObject two;
    private Vector2 epicentre;
    private float time;
    private float impact;

    //TODO: Two,  maybe needs to be a list of PhysicalObjects ===> more objects can hit another object at the same time?
    public Collision(PhysicalObject one, PhysicalObject two, Vector2 epicentre, float time, float impact) {
        this.one = one;
        this.two = two;
        this.epicentre = epicentre;
        this.time = time;
        this.impact = impact;
    }

    public PhysicalObject getOne() {
        return one;
    }

    public PhysicalObject getTwo() {
        return two;
    }

    public Vector2 getEpicentre() {
        return epicentre;
    }

    public float getTime() {
        return time;
    }

    public float getImpact() {
        return impact;
    }
}
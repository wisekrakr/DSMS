package com.wisekrakr.firstgame.engine.gameobjects.spaceobjects;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.testcrap.MoveAlongAction;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Rotunda extends GameObject {

    private float rotationSpeed;
    private float angle;
    private float radius;
    private float time;

    public Rotunda(String name, Vector2 position, SpaceEngine space, float radius, float angle ) {
        super(name, position, space);
        this.angle = angle;
        this.radius = radius;
        setCollisionRadius(radius);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {


    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }
}

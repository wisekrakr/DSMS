package com.wisekrakr.firstgame.engine.gameobjects.spaceobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Rotunda extends GameObject {

    private float rotationSpeed;
    private float angle;
    private float radius;
    private float time;

    public Rotunda(String name, Vector2 position, float radius, float angle ) {
        super(GameObjectVisualizationType.ROTUNDA, name, position);
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

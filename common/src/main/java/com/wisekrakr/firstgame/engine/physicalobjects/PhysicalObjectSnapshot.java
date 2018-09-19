package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.Map;

public class PhysicalObjectSnapshot implements Serializable {
    private String name;
    private Visualizations visualization;

    private float speedDirection;
    private float speedMagnitude;
    private float orientation;
    private Vector2 position;
    private Map<String, Object> extra;

    public PhysicalObjectSnapshot(String name, Visualizations visualization, float speedDirection, float speedMagnitude, float orientation, Vector2 position, Map<String, Object> extra) {
        this.name = name;
        this.visualization = visualization;
        this.speedDirection = speedDirection;
        this.speedMagnitude = speedMagnitude;
        this.orientation = orientation;
        this.position = position;
        this.extra = extra;
    }

    public PhysicalObjectSnapshot() {
    }

    public String getName() {
        return name;
    }

    public Visualizations getVisualization() {
        return visualization;
    }

    public float getSpeedDirection() {
        return speedDirection;
    }

    public float getSpeedMagnitude() {
        return speedMagnitude;
    }

    public float getOrientation() {
        return orientation;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }
}

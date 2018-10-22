package com.wisekrakr.firstgame.engine.physicalobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class PhysicalObjectRunner implements PhysicalObject {
    private String name;

    private Vector2 position;
    private float orientation;

    private float speedMagnitude;
    private float speedDirection;

    private float health;
    private float damage;
    private Visualizations visualization;
    private Map<String, Object> extraProperties;
    private float collisionRadius;
    private PhysicalObjectListener listener;

    public PhysicalObjectRunner(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, float health, float damage, Visualizations visualization, Map<String, Object> extraProperties, float collisionRadius, PhysicalObjectListener listener) {
        this.name = name;
        this.position = position;
        this.orientation = orientation;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.health = health;
        this.damage = damage;
        this.visualization = visualization;
        this.extraProperties = new HashMap<>(extraProperties);
        this.collisionRadius = collisionRadius;
        this.listener = listener;
        if (listener == null) {
            this.listener = new AbstractPhysicalObjectListener() {
            };
        }
    }

    public String getName() {
        return name;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getOrientation() {
        return orientation;
    }

    public float getSpeedMagnitude() {
        return speedMagnitude;
    }

    public float getSpeedDirection() {
        return speedDirection;
    }

    public Visualizations getVisualization() {
        return visualization;
    }

    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }

    @Override
    public float getCollisionRadius() {
        return collisionRadius;
    }

    @Override
    public float getDamage() {
        return damage;
    }

    @Override
    public float getHealth() {
        return health;
    }


    public PhysicalObjectSnapshot snapshot() {
        return new PhysicalObjectSnapshot(
                name,
                visualization,
                health,
                damage,
                speedDirection,
                getSpeedMagnitude(),
                orientation,
                position,
                extraProperties);
    }

    public void update(String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualization, Float collisionRadius, Float health, Float damage) {
        if (name != null) {
            this.name = name;
        }

        if (position != null) {
            this.position = position;
        }

        if (orientation != null) {
            this.orientation = orientation;
        }

        if (speedMagnitude != null) {
            this.speedMagnitude = speedMagnitude;
        }

        if (speedDirection != null) {
            this.speedDirection = speedDirection;
        }

        if (visualization != null) {
            this.visualization = visualization;
        }

        if (collisionRadius != null) {
            this.collisionRadius = collisionRadius;
        }
        if (health != null) {
            this.health = health;
        }
        if (damage != null) {
            this.damage = damage;
        }
    }

    public void updateExtra(String key, Object value) {
        if (value == null) {
            extraProperties.remove(key);
        } else {
            extraProperties.put(key, value);
        }
    }

    public PhysicalObjectListener getListener() {
        return listener;
    }


}

package com.wisekrakr.firstgame.engine.gameobjects.spaceobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Debris extends GameObject {

    private float rotationSpeed;
    private float speed;
    private float direction;
    private float radius;
    private float time;

    public Debris(Vector2 initialPosition, float rotationSpeed, float speed, float direction, float radius) {

        super(GameObjectVisualizationType.DEBRIS, "Debris", initialPosition);
        this.rotationSpeed = rotationSpeed;
        this.speed = speed;
        this.direction = direction;
        this.radius = radius;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        setOrientation(getOrientation() + rotationSpeed * delta);
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * speed * delta,
                getPosition().y + (float) Math.sin(direction) * speed * delta)
        );

        float destructTime = 3f;
        time += delta;
        if(time >= destructTime) {
            toDelete.add(this);
        }
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.spaceshipparts;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Exhaust extends GameObject {

    private float direction;
    private float radius;

    private static final float DEFAULT_SPEED = 50;
    private float time;

    public Exhaust(String name, Vector2 initialPosition, float direction, float radius) {
        super(GameObjectVisualizationType.EXHAUST, name, initialPosition);
        this.direction = direction;
        this.radius = radius;
        setCollisionRadius(radius);

    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x - (float) Math.cos(direction) * DEFAULT_SPEED * delta,
                getPosition().y - (float) Math.sin(direction) * DEFAULT_SPEED * delta)
        );
        setOrientation(direction);

        float destructTime = 0.2f;
        time += delta;
        if(time >= destructTime){
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

package com.wisekrakr.firstgame.engine.gameobjects.spaceshipparts;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VisionCone extends GameObject {

    private float direction;
    private float radius;

    private static final float DEFAULT_SPEED = 200;
    private float time;

    private Spaceship.AimingState aimingState = Spaceship.AimingState.NONE;

    public VisionCone(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius) {
        super(name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_SPEED * delta)
        );
        setOrientation(direction);

        float destructTime = 0.3f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

        switch (aimingState){
            case NONE:
                break;

            case TWELVE:
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_SPEED * delta,
                        getPosition().y + (float) Math.sin(direction) * DEFAULT_SPEED * delta)
                );
                setOrientation(direction);
                break;
            case SIX:
                setPosition(new Vector2(getPosition().x - (float) Math.cos(direction) * DEFAULT_SPEED * delta,
                        getPosition().y - (float) Math.sin(direction) * DEFAULT_SPEED * delta)
                );
                setOrientation(direction);
            case THREE:
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction + Math.PI/2) * DEFAULT_SPEED * delta,
                        getPosition().y + (float) Math.sin(direction + Math.PI/2) * DEFAULT_SPEED * delta)
                );
                setOrientation(direction);
                break;
            case NINE:
                setPosition(new Vector2(getPosition().x - (float) Math.cos(direction + Math.PI/2) * DEFAULT_SPEED * delta,
                        getPosition().y - (float) Math.sin(direction + Math.PI/2) * DEFAULT_SPEED * delta)
                );
                setOrientation(direction);
                break;

        }


    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }
}

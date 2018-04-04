package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeamCannon extends GameObject {

    private float direction;
    private float radius;

    private static final float DEFAULT_SPEED = 200;
    private float time;

    public Spaceship.SpecialPowerState powerState = Spaceship.SpecialPowerState.NO_POWER;

    public BeamCannon(String name, Vector2 initialPosition, SpaceEngine space, float direction, float radius) {
        super(name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (powerState == Spaceship.SpecialPowerState.BEAM) {
            setOrientation(direction); // this way i can choose a angle and shoot of that way
            setPosition(new Vector2(
                    getPosition().x + delta * DEFAULT_SPEED * direction,
                    getPosition().y + delta * DEFAULT_SPEED * direction
            ));
        }else {
            setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_SPEED * delta,
                    getPosition().y + (float) Math.sin(direction) * DEFAULT_SPEED * delta)
            );
            setOrientation(direction);
        }
        float destructTime = 5f;
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

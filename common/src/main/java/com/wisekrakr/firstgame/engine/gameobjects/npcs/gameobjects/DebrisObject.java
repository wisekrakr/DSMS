package com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.SplashBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DebrisObject extends GameObject {

    private float timeToSplash;
    private boolean initialize = false;
    private float speed = 0f;

    public DebrisObject(Vector2 initialPosition, float initialRadius) {
        super(GameObjectVisualizationType.DEBRIS, "debris", initialPosition);
        setCollisionRadius(initialRadius);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", getCollisionRadius());

        return result;
    }

    @Override
    public void afterAdd(List<GameObject> toAdd, List<GameObject> toRemove) {
        if (getCollisionRadius() <= 0.5f){
            toRemove.add(this);
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (timeToSplash == 0){
            timeToSplash = clock;
        }

        setOrientation(GameHelper.randomDirection());

        if (!initialize) {
            setDirection(GameHelper.randomGenerator.nextFloat() * 2f * (float) Math.PI);
            speed = GameHelper.randomGenerator.nextFloat() * 25f;
            initialize = true;
        }

        setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * speed * delta,
                getPosition().y + (float) Math.sin(getDirection()) * speed * delta)
        );

        if (clock - timeToSplash >= 2){
            toDelete.add(this);
            timeToSplash = clock;
        }
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.spaceobjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.FaceHuggingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.Protector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Package extends GameObject {

    private float speed = GameHelper.generateRandomNumberBetween(20f, 30f);
    private Float timeCounter;

    public Package(Vector2 initialPosition) {
        super(GameObjectVisualizationType.POWERUP_HEALTH, "Package", initialPosition);
        setCollisionRadius(3f);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", getCollisionRadius());
        result.put("speed", speed);

        return result;
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
/*
        if(subject instanceof Player){
            toDelete.add(this);
        }
        */
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (timeCounter == null){
            timeCounter = clock;
        }

        if (clock - timeCounter > 5) {


            timeCounter = clock;
        }

    }
}

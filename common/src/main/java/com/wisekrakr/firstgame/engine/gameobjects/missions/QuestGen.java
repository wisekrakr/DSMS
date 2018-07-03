package com.wisekrakr.firstgame.engine.gameobjects.missions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QuestGen extends GameObject {

    private static final String TAG = "";

    private float lastQuestPop = -100000f;

    public QuestGen(Vector2 initialPosition) {
        super(GameObjectType.TEST_QUEST, "Quest Generator", initialPosition);
        setCollisionRadius(20f);
                
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        /*
        if (pickedUp) {
            if (clock - lastQuestPop > 10) {
                System.out.println(TAG + getPosition());
                toAdd.add(new QuestGen(new Vector2(0, 0), getSpace()));
                lastQuestPop = clock;
                pickedUp = false;
            }
        }
        */
    }


    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", getCollisionRadius());

        return result;
    }

}

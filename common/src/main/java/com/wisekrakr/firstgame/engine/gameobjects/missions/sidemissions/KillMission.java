package com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KillMission extends Mission {

    private boolean pickedUp;

    public KillMission(Vector2 initialPosition, String targets) {
        super(GameObjectVisualizationType.TEST_QUEST, targets, initialPosition);
        setCollisionRadius(3f);

    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", getCollisionRadius());
        result.put("pickedUp", pickedUp);
        result.put("name", getName());

        return result;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player){
            if (!pickedUp) {
                pickedUp = true;
            }else {
                toDelete.add(this);
            }

        }
    }


}
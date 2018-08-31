package com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PackageDeliveryMission extends Mission {
    private boolean pickedUp;

    public PackageDeliveryMission(Vector2 initialPosition) {
        super(GameObjectVisualizationType.TEST_QUEST, "Deliver Package Mission", initialPosition);
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

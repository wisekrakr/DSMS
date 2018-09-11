package com.wisekrakr.firstgame.engine.gameobjects.missions;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.KillMission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.PackageDeliveryMission;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Mission extends GameObject {

    private boolean pickedUp;

    public Mission(String name, Vector2 initialPosition) {
        super(GameObjectVisualizationType.TEST_QUEST, name, initialPosition);
        setCollisionRadius(10f);
    }

    public Mission(GameObjectVisualizationType type, String name, Vector2 initialPosition) {
        this(name, initialPosition);

    }

    public String className(){
        String name = "";
        if (this instanceof KillMission){
            name = KillMission.class.getName();
        }else if (this instanceof PackageDeliveryMission){
            name = PackageDeliveryMission.class.getName();
        }
        return name;
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

    public boolean isPickedUp() {
        return pickedUp;
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
/*
        if (timeCounter == 0){
            timeCounter = clock;
        }
        if (clock - timeCounter > 10){
            toDelete.add(this);
            timeCounter = clock;
        }
        */
    }
}

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

    public PackageDeliveryMission(Vector2 initialPosition, String targetName) {
        super(GameObjectVisualizationType.TEST_QUEST, targetName, initialPosition);

    }

}

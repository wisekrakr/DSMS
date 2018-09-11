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
    public KillMission(Vector2 initialPosition, String targets) {
        super(GameObjectVisualizationType.TEST_QUEST, targets, initialPosition);

    }
}

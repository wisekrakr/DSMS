package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestObject extends WeaponObjectClass {

    public TestObject(Vector2 initialPosition, Behavior initialBehavior, GameObject master) {
        super(GameObjectVisualizationType.EXHAUST, "fireworks", initialPosition, initialBehavior, master);
        this.setCollisionRadius(GameHelper.generateRandomNumberBetween(1f, 6f));
        this.setDamage(this.getCollisionRadius());
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", getCollisionRadius());

        return result;
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.RotatingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.StickyBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.weaponbehaviors.HomingMissileBehavior;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PackageObject extends WeaponObjectClass{

    public PackageObject(Vector2 initialPosition, GameObject master) {
        super(GameObjectVisualizationType.SPORE, "Package", initialPosition, master);

        this.rootBehavior(new RotatingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f)));

        this.setDimensions(14f, 14f);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("width", getWidth());
        result.put("height", getHeight());


        return result;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player){
            this.rootBehavior(new StickyBehavior(subject));
        }
    }
}

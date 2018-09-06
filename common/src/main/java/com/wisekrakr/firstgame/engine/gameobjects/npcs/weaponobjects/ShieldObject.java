package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShieldObject extends GameObject {

    private GameObject master;

    public ShieldObject(Vector2 initialPosition, GameObject master) {
        super(GameObjectVisualizationType.SHIELD, "shield", initialPosition);
        this.master = master;

        this.setCollisionRadius(master.getCollisionRadius() * 2);
        this.setHealth(100f);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", getCollisionRadius());

        return result;
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof WeaponObjectClass){
            toDelete.add(subject);
            this.setHealth(this.getHealth() - subject.getDamage());
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(master.getPosition());
        setOrientation(master.getOrientation());
        setDirection(master.getDirection());
    }
}

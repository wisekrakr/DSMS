package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Shield extends GameObject {

    private float speed;
    private float radius;
    private float direction;
    private float time;
    private float timeActivated;

    public Shield(String name, Vector2 initialPosition, float direction, float speed, float radius) {
        super(GameObjectVisualizationType.SHIELD, name, initialPosition);
        this.direction = direction;
        this.speed = speed;
        this.radius = radius;

        setCollisionRadius(radius);
        setHealth(100);
        setTimeActivated(20);

    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Enemy) {
            subject.setHealth(subject.getHealth() - getDamage());
            setHealth(getHealth() - 10);
        }
        if (subject instanceof WeaponObjectClass) {
            toDelete.add(subject);
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        time += delta;

        if (time >= timeActivated) {
            toDelete.add(this);
            time = 0;
        }

    }

    public float getTimeActivated() {
        return timeActivated;
    }

    public void setTimeActivated(float timeActivated) {
        this.timeActivated = timeActivated;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }

}

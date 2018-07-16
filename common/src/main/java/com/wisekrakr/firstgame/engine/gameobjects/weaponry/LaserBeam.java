package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LaserBeam extends GameObject {

    private float direction;
    private float radius;
    private float speed;
    private int damage;

    private float time;

    public LaserBeam(String name, Vector2 initialPosition, float direction, float radius, int damage, float speed) {
        super(GameObjectVisualizationType.LASER_BEAM, name, initialPosition);
        this.radius = radius;
        this.damage = damage;
        this.direction = direction;
        this.speed = speed;

        setCollisionRadius(radius);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Player){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
            if (((Player) subject).isKilled()){
                ((Player) subject).setKillerName(this.getName());
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * getSpeed() * delta,
                getPosition().y + (float) Math.sin(direction) * getSpeed() * delta)
        );
        setOrientation(direction);

        float destructTime = 1.5f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }
    }

    public float getRadius() {
        return radius;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }
}

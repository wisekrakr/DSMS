package com.wisekrakr.firstgame.engine.gameobjects.weaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Weapons;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class PlayerMissile extends GameObject {

    private float direction;
    private float radius;
    private float time;
    private float speed;

    private static final float ATTACK_RANGE = 300;
    private static final float DEFAULT_MISSILE_SPEED = 800;

    public PlayerMissile(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius) {
        super(name, initialPosition, space);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;

        setCollisionRadius(5);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof Enemy){
            subject.setHealth(getHealth() - 25);
            toDelete.add(this);
        }
    }

    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(target instanceof Enemy) {

            if(distanceBetween(this, target)< ATTACK_RANGE){
                attackTarget(target, toDelete, toAdd);
            }
        }


    }

    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Enemy) {

            float angle = angleBetween(this, subject);

            // to make the chaser chase the player with less vigilance, divide cos and sin by 2
            setPosition(new Vector2(getPosition().x += Math.cos(angle), getPosition().y += Math.sin(angle)));

            setOrientation(angle);

            setDirection(angle);


        }
    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_MISSILE_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_MISSILE_SPEED * delta)
        );
        setOrientation(direction);

        float destructTime = 3.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        return super.getExtraSnapshotProperties();
    }

    public float getRadius() {
        return radius;
    }
}

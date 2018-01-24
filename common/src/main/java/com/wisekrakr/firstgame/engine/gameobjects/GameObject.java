package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by David on 11/6/2017.
 */
public abstract class GameObject {
    private String name;
    private Vector2 position;
    private float orientation;
    private float speed;
    private SpaceSnapshot snapshot;
    private SpaceEngine space;
    private float collisionRadius;
    private float distanceBetween;
    private Integer distanceTravelled;

    GameObject(String name, Vector2 initialPosition, SpaceEngine space) {
        this.position = initialPosition;
        this.name = name;
        this.space = space;
    }

    /**
     * update the state taking into account an elapsed time of delta seconds
     */
    public abstract void elapseTime(float delta);

    public final float getOrientation() {
        return orientation;
    }

    public final Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setCollisionRadius(float radius) {
        this.collisionRadius = radius;
    }


    public void setOrientation(float newOrientation) {
        this.orientation = newOrientation;
    }

    public SpaceEngine getSpace() {
        return space;
    }
    public String getName() {
        return name;
    }

    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }
    public void signalOutOfBounds() {
    }

    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }

    public float distanceBetween(GameObject subject, GameObject target){


        if(target instanceof Player){

            float attackDistanceX = target.getPosition().x - subject.getPosition().x;
            float attackDistanceY = target.getPosition().y - subject.getPosition().y ;

            distanceBetween = (float) Math.hypot(attackDistanceX, attackDistanceY);
        }

        return distanceBetween;
    }
    public float angleBetween(GameObject subject, GameObject target){

        float attackDistanceX = target.getPosition().x - subject.getPosition().x;
        float attackDistanceY = target.getPosition().y - subject.getPosition().y;

        float angle = (float) Math.atan2(attackDistanceY, attackDistanceX);

        return angle;
    }

    public void attack(GameObject target){

    }

    public void shootingBullets(GameObject bullet, Set<GameObject>toAdd, Set<GameObject>toDelete){}


    public void objectRemover(Set<GameObject>toDelete){

    }


    public Integer getDistanceTravelled() {

        if (snapshot != null) {
            for (SpaceSnapshot.GameObjectSnapshot object : snapshot.getGameObjects()) {
                if ("Player".equals(object.getType())) {
                    float newX = object.getPosition().x;
                    float newY = object.getPosition().y;

                    distanceTravelled = (int) Math.sqrt(Math.pow(newX - object.getPosition().x, 2) + Math.pow(newY - object.getPosition().y, 2));
                }
            }
        }

/*
        if(subject instanceof Player) {
            Vector2 newPosition = subject.getPosition();

            distanceTravelled = (int) Math.sqrt(Math.pow(newPosition.x - subject.getPosition().x, 2) + Math.pow(newPosition.y - subject.getPosition().y, 2));
        }
        */
        return distanceTravelled;
    }


    public float getCollisionRadius() {
        return collisionRadius;
    }

    public SpaceSnapshot.GameObjectSnapshot snapshot() {
        return new SpaceSnapshot.GameObjectSnapshot(name, getClass().getSimpleName(), 0, orientation, position, getExtraSnapshotProperties());
    }

    public Map<String, Object> getExtraSnapshotProperties() {
        return new HashMap<String, Object>();
    }



}

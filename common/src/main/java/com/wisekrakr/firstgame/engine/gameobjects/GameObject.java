package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

import java.util.*;


/**
 * Created by David on 11/6/2017.
 */
public abstract class GameObject{
    private String name;
    private Vector2 position;
    private float orientation;
    private float speed;
    private SpaceSnapshot snapshot;
    private SpaceEngine space;
    private float collisionRadius;
    private int health;
    private int score;

    protected GameObject(String name, Vector2 initialPosition, SpaceEngine space) {

        this.position = initialPosition;
        this.name = name;
        this.space = space;

    }

    /**
     * update the state taking into account an elapsed time of delta seconds
     */
    public abstract void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd);

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

    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }
    public void scoring(GameObject player, GameObject subject){}

    public int randomScore(int damage){

        return damage;
    }

    public int randomDamageCountBullet(){
        Random random = new Random();
        return random.nextInt(10) + 1;
    }
    public int randomDamageCountMissile(){
        Random random = new Random();
        return random.nextInt(20 - 10 + 1) + 10;
    }

    public float setRandomDirection(){
        Random random = new Random();
        return random.nextFloat() * 2000 - 1000;
    }

    public static float distanceBetween(GameObject subject, GameObject target) {
        float attackDistanceX = target.getPosition().x - subject.getPosition().x;
        float attackDistanceY = target.getPosition().y - subject.getPosition().y;

        return (float) Math.hypot(attackDistanceX, attackDistanceY);
    }

    public static float angleBetween(GameObject subject, GameObject target) {

        float attackDistanceX = target.getPosition().x - subject.getPosition().x;
        float attackDistanceY = target.getPosition().y - subject.getPosition().y;

        float angle = (float) Math.atan2(attackDistanceY, attackDistanceX);

        return angle;
    }


    public void getsDestroyed(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd){}

    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd){}


    public void targetSpotted(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd){}
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd){}

    public float getCollisionRadius() {
        return collisionRadius;
    }

    public SpaceSnapshot.GameObjectSnapshot snapshot() {
        return new SpaceSnapshot.GameObjectSnapshot(name, getClass().getSimpleName(), 0, orientation, position,
                getExtraSnapshotProperties(), getAmmoProperties(), getHealthProperties(), getScoreProperties(), getMissileProperties());
    }

    public GameObject exhaust(Vector2 initialPosition, SpaceEngine space){

        return exhaust(this.getPosition(), getSpace());

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Map<String, Object> getExtraSnapshotProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getAmmoProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getHealthProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getScoreProperties() {
        return new HashMap<>();
    }

    public Map<String, Object> getMissileProperties() {
        return new HashMap<>();
    }


}

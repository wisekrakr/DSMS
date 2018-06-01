package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;

import java.util.*;


/**
 * Created by David on 11/6/2017.
 */
public abstract class GameObject {

    private float enemyMarginOfError;
    private String name;
    private Vector2 position;
    private float orientation;
    private SpaceEngine space;
    private float collisionRadius;
    private int health;
    private GameObjectType type;

    protected GameObject(GameObjectType type, String name, Vector2 initialPosition, SpaceEngine space) {
        this.type = type;
        this.position = initialPosition;
        this.name = name;
        this.space = space;
    }
    /**
     * update the state taking into account an elapsed time of delta seconds
     */
    public abstract void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd);
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
    public float getEnemyMarginOfError() {
        return enemyMarginOfError;
    }
    public void setEnemyMarginOfError(float enemyMarginOfError) {
        this.enemyMarginOfError = enemyMarginOfError;
    }
    public SpaceEngine getSpace() {
        return space;
    }
    public String getName() {
        return name;
    }
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {    }
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {    }
    public boolean isHit(GameObject object){
            return
                    Math.sqrt(
                            (((this.getPosition().x) - (object.getPosition().x)))
                                    * ((this.getPosition().x) - (object.getPosition().x))
                                    + ((this.getPosition().y) - (object.getPosition().y))
                                    * ((this.getPosition().y) - (object.getPosition().y)))
                            < (this.getCollisionRadius() + object.getCollisionRadius());

    }

    public float setRandomDirection(){
        Random random = new Random();
        return random.nextFloat() * 2000 - 1000;
    }

    public float setRandomDirectionStartScreen(){
        Random random = new Random();
        return random.nextFloat() * 1200;
    }

    public static float distanceBetween(GameObject subject, GameObject target) {

        float attackDistanceX = target.getPosition().x - subject.getPosition().x;
        float attackDistanceY = target.getPosition().y - subject.getPosition().y;

        return (float) Math.hypot(attackDistanceX, attackDistanceY);
    }

    public static float angleBetween(GameObject subject, GameObject target) {

        float attackDistanceX = target.getPosition().x - subject.getPosition().x;
        float attackDistanceY = target.getPosition().y - subject.getPosition().y;

        return (float) Math.atan2(attackDistanceY, attackDistanceX);
    }
    public float angleBetweenNoAim(GameObject subject, GameObject target) {
        Random random = new Random();
        setEnemyMarginOfError(60f);
        float number = random.nextFloat() * enemyMarginOfError;

        float attackDistanceX = (target.getPosition().x + number) - subject.getPosition().x;
        float attackDistanceY = (target.getPosition().y + number) - subject.getPosition().y;

        return (float) Math.atan2(attackDistanceY, attackDistanceX);
    }

    public void overlappingObjects(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd){

    }


    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd){}
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd){}
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd){}
    public float getCollisionRadius() {
        return collisionRadius;
    }

    public SpaceSnapshot.GameObjectSnapshot snapshot() {
        return new SpaceSnapshot.GameObjectSnapshot(name, type, 0, orientation, position,
                getExtraSnapshotProperties(), getAmmoProperties(), getHealthProperties(), getScoreProperties(),
                getDamageProperties(), getRandomProperties(), getKilledByProperties());
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

    public Map<String, Object> getDamageProperties() {
        return new HashMap<>();
    }


    public Map<String, Object> getRandomProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getKilledByProperties() {
        return new HashMap<>();
    }


    public void afterAdd(List<GameObject> toAdd) {
    }

    public void afterRemove(List<GameObject> toAdd, List<GameObject> toRemove) {
    }
}

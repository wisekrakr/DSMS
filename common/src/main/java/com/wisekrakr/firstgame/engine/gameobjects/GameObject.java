package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;

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
    private float health;
    private int damage;
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
    public GameObjectType getType() {
        return type;
    }
    public void setType(GameObjectType type) {
        this.type = type;
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
    public void initDebris(Set<GameObject> toDelete, Set<GameObject> toAdd){
        Random random = new Random();
        int debrisParts = random.nextInt(10)+1;
        for(int i = 0; i < debrisParts; i++) {
            toAdd.add(new Debris("debris", this.getPosition(), getSpace(), random.nextFloat() * 10,
                    random.nextFloat() * 60, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getCollisionRadius()));

        }
    }
    public boolean collisionDetected(GameObject object1, GameObject object2){
            return
                    Math.sqrt(
                            (((object1.getPosition().x) - (object2.getPosition().x)))
                                    * ((object1.getPosition().x) - (object2.getPosition().x))
                                    + ((object1.getPosition().y) - (object2.getPosition().y))
                                    * ((object1.getPosition().y) - (object2.getPosition().y)))
                            < (object1.getCollisionRadius() + object2.getCollisionRadius());

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
                getExtraSnapshotProperties(), getAmmoProperties(), getHealthProperties(), getMaxHealthProperties(), getScoreProperties(),
                getDamageProperties(), getDamageTakenProperties(), getRandomProperties(), getKilledByProperties(), getHitProperties());
    }


    public float getHealth() {
        return health;
    }
    public void setHealth(float health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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
    public Map<String, Object> getMaxHealthProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getScoreProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getDamageProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getDamageTakenProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getRandomProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getKilledByProperties() {
        return new HashMap<>();
    }
    public Map<String, Object> getHitProperties() {
        return new HashMap<>();
    }

    public void afterAdd(List<GameObject> toAdd) {
    }

    public void afterRemove(List<GameObject> toAdd, List<GameObject> toRemove) {
    }
}

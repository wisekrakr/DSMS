package com.wisekrakr.firstgame.engine.gameobjects;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.SpaceSnapshot;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;

import java.util.*;


public abstract class GameObject {

    private String name;
    private Vector2 position;
    private float orientation;
    private float direction;
    private float collisionRadius;
    private float width, height;
    private double health;
    private double damage;
    private GameObjectVisualizationType type;
    private float actionDistance;


    protected GameObject(GameObjectVisualizationType type, String name, Vector2 initialPosition) {
        this.type = type;
        this.position = initialPosition;
        this.name = name;
    }

    public void nearby(List<GameObject> targets) {

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

    public GameObjectVisualizationType getType() {
        return type;
    }

    public void setType(GameObjectVisualizationType type) {
        this.type = type;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getActionDistance() {
        return actionDistance;
    }

    public void setActionDistance(float actionDistance) {
        this.actionDistance = actionDistance;
    }

    public String getName() {
        return name;
    }

    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }

    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }

    public void initDebris(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        Random random = new Random();
        int debrisParts = random.nextInt(10) + 1;
        for (int i = 0; i < debrisParts; i++) {
            toAdd.add(new Debris(this.getPosition(), random.nextFloat() * 10,
                    random.nextFloat() * 60, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getCollisionRadius()));

        }
    }

    public GameObject thisGameObject() {
        return this;
    }

    public void overlappingObjects(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

    }

    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }

    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }

    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
    }

    public float getCollisionRadius() {
        return collisionRadius;
    }

    public SpaceSnapshot.GameObjectSnapshot snapshot() {
        return new SpaceSnapshot.GameObjectSnapshot(name, type, 0, orientation, position,
                getExtraSnapshotProperties());
    }

    public Map<String, Object> getExtraSnapshotProperties() {
        return Collections.emptyMap();
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setDimensions(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void afterAdd(List<GameObject> toAdd, List<GameObject> toRemove) {
    }

    public void afterRemove(List<GameObject> toAdd, List<GameObject> toRemove) {
    }
}

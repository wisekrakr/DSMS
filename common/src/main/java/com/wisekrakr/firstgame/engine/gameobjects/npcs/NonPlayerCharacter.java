package com.wisekrakr.firstgame.engine.gameobjects.npcs;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.AsteroidNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;


import java.util.*;

public class NonPlayerCharacter extends GameObject {
    private List<Behavior> activeBehaviors = new ArrayList<>();
    private float speed = 0f;
    private List<GameObject> nearby;

    private float actionDistance = 0f;

    private GameObject cachedNearest;
    private float distanceInFloats;

    protected NonPlayerCharacter(GameObjectVisualizationType type, String name, Vector2 initialPosition) {
        super(type, name, initialPosition);
    }

    protected NonPlayerCharacter(GameObjectVisualizationType type, String name, Vector2 initialPosition, Behavior initialBehavior) {
        this(type, name, initialPosition);

        rootBehavior(initialBehavior);
    }

    protected final void rootBehavior(Behavior initialBehavior) {
        activeBehaviors.add(initialBehavior);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("health", getHealth());
        result.put("maxHealth", getHealth());
        result.put("radius", getCollisionRadius());
        result.put("healthPercentage", 1d);
        result.put("damage", getDamage());
        result.put("actionDistance", actionDistance);
        result.put("speed", speed);
        result.put("direction", getDirection());

        return result;
    }

    @Override
    public void nearby(List<GameObject> targets) {
        this.nearby = targets;
        cachedNearest = null;
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        setDirection(this.getDirection() + (float) Math.PI);
        setOrientation(this.getDirection());
    }

    private void keepObjectsFromOverlapping(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd){
        if (!(subject instanceof WeaponObjectClass || subject instanceof DebrisObject || subject instanceof Mission || subject instanceof AsteroidNPC)) {
            float angle = GameHelper.angleBetween(this, subject);
            if (GameHelper.distanceBetween(this, subject) <= getCollisionRadius() + subject.getCollisionRadius()) {
                setPosition(new Vector2(getPosition().x -= Math.cos(angle) * GameHelper.randomGenerator.nextFloat() * 12.5,
                        getPosition().y -= Math.sin(angle) * GameHelper.randomGenerator.nextFloat() * 12.5));

                setOrientation(-angle);
                setDirection(getDirection() + (float) Math.PI);
            }
        }
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        keepObjectsFromOverlapping(subject, toDelete, toAdd);
    }

    @Override
    public final void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        int index = 0;

        while (index < activeBehaviors.size()) {
            Behavior b = activeBehaviors.get(index);

            int myIndex = index;

            b.elapseTime(clock, delta, new BehaviorContext() {
                @Override
                public void addGameObject(GameObject newObject) {
                    toAdd.add(newObject);
                }

                @Override
                public void removeGameObject(GameObject object) {
                    toDelete.add(object);
                }

                @Override
                public void pushSubBehavior(Behavior b) {
                    while (activeBehaviors.size() > myIndex + 1) {
                        activeBehaviors.remove(activeBehaviors.size() - 1);
                    }

                    activeBehaviors.add(b);
                }

                @Override
                public Behavior existingSubBehavior() {
                    if (activeBehaviors.size() > myIndex + 1) {
                        return activeBehaviors.get(myIndex + 1);
                    }

                    return null;
                }


                @Override
                public List<GameObject> nearby() {
                    return nearby;
                }

                @Override
                public GameObject nearest() {
                    return determineNearest();
                }

                @Override
                public float nearestInFloats() {
                    return distanceInFloats();
                }

                @Override
                public GameObject thisObject() {
                    return NonPlayerCharacter.this.thisGameObject();
                }

                @Override
                public Vector2 getPosition() {
                    return NonPlayerCharacter.this.getPosition();
                }

                @Override
                public float getSpeed() {
                    return speed;
                }

                @Override
                public float getOrientation() {
                    return NonPlayerCharacter.this.getOrientation();
                }

                @Override
                public float getDirection() {
                    return NonPlayerCharacter.this.getDirection();
                }

                @Override
                public void setOrientation(float orientation) {
                    NonPlayerCharacter.this.setOrientation(orientation);
                }

                @Override
                public void setDirection(float direction) {
                    NonPlayerCharacter.this.setDirection(direction);
                }

                @Override
                public double getHealth() {
                    return NonPlayerCharacter.this.getHealth();
                }

                @Override
                public void setHealth(double health) {
                    NonPlayerCharacter.this.setHealth(health);
                }

                @Override
                public float getRadius() {
                    return NonPlayerCharacter.this.getCollisionRadius();
                }

                @Override
                public void setRadius(float radius) {
                    NonPlayerCharacter.this.setCollisionRadius(radius);
                }

                @Override
                public float getActionDistance() {
                    return actionDistance;
                }

                @Override
                public void setActionDistance(float actionDistance) {
                    NonPlayerCharacter.this.setActionDistance(actionDistance);
                }

                @Override
                public void setSpeed(float speed) {
                    NonPlayerCharacter.this.speed = speed;
                }

                @Override
                public boolean collisionDetection(GameObject object) {
                    return
                            Math.sqrt(
                                    (((NonPlayerCharacter.this.getPosition().x) - (object.getPosition().x)))
                                            * ((NonPlayerCharacter.this.getPosition().x) - (object.getPosition().x))
                                            + ((NonPlayerCharacter.this.getPosition().y) - (object.getPosition().y))
                                            * ((NonPlayerCharacter.this.getPosition().y) - (object.getPosition().y)))
                                    < (NonPlayerCharacter.this.getCollisionRadius() + object.getCollisionRadius());
                }
            });

            index = index + 1;
        }

        // TODO: move towards the general infrastructure
        setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * speed * delta,
                getPosition().y + (float) Math.sin(getDirection()) * speed * delta)
        );
    }


    private float distanceInFloats() {

        for (GameObject object : nearby) {
            distanceInFloats = GameHelper.distanceBetween(this, object);
        }
        return distanceInFloats;
    }

    private GameObject determineNearest() {
        if (cachedNearest == null) {
            float bestDistance = 0f;
            for (GameObject object : nearby) {
                float distance = GameHelper.distanceBetween(this, object);
                if (cachedNearest == null || distance < bestDistance) {
                    bestDistance = distance;
                    cachedNearest = object;
                }
            }
        }
        return cachedNearest;
    }

}

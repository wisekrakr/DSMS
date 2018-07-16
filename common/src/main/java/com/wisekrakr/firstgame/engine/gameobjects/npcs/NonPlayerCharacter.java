package com.wisekrakr.firstgame.engine.gameobjects.npcs;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.mechanics.EnemyMechanics;

import java.util.*;

public class NonPlayerCharacter extends GameObject {
    private List<Behavior> activeBehaviors = new ArrayList<>();
    private float direction = 0f;
    private float speed = 0f;
    private List<GameObject> nearby;

    private GameObject cachedNearest;

    protected NonPlayerCharacter(GameObjectVisualizationType type, String name, Vector2 initialPosition, Behavior initialBehavior) {
        super(type, name, initialPosition);

        activeBehaviors.add(initialBehavior);
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("health", 10f);
        result.put("maxHealth", 10f);
        result.put("radius", 4f);
        result.put("healthPercentage", 1f);

        return result;
    }

    @Override
    public void nearby(List<GameObject> targets) {
        this.nearby = targets;
        cachedNearest = null;
    }

    @Override
    public void targetSpotted(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.targetSpotted(target, toDelete, toAdd);
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
                    return direction;
                }

                @Override
                public void setOrientation(float orientation) {
                    NonPlayerCharacter.this.setOrientation(orientation);
                }

                @Override
                public void setDirection(float randomDirection) {
                    NonPlayerCharacter.this.direction = randomDirection;
                }

                @Override
                public void setSpeed(float speed) {
                    System.out.println("Speed now: " + speed);
                    NonPlayerCharacter.this.speed = speed;
                }
            });

            index = index + 1;
        }


        // TODO: move towards the general infrastructure
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * speed * delta,
                getPosition().y + (float) Math.sin(direction) * speed * delta)
        );
    }


    private GameObject determineNearest() {
        if (cachedNearest == null) {
            float bestDistance = 0f;

            for (GameObject object: nearby) {
                float distance = GameHelper.distanceBetween(this, object);
                if (cachedNearest == null || distance < bestDistance) {
                    bestDistance  = distance;
                    cachedNearest = object;
                }
            }

        }
        return cachedNearest;
    }
}

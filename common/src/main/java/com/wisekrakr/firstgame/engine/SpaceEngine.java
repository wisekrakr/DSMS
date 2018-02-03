package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpaceEngine {
    private final Object monitor = new Object();
    private final float minX;
    private final float minY;
    private final float width;
    private final float height;
    private Set<GameObject> gameObjects = new HashSet<GameObject>();

    public SpaceEngine(float minX, float minY, float width, float height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    public void addGameObject(GameObject object) {
        synchronized (monitor) {
            gameObjects.add(object);
        }
    }

    public void removeGameObject(GameObject object) {
        synchronized (monitor) {
            gameObjects.remove(object);
        }
    }

    public void forObject(GameObject object, GameObjectHandler action) {
        synchronized (monitor) {
            action.doIt(object);
        }
    }

    public void forAllObjects(GameObjectHandler action) {
        synchronized (monitor) {
            for (GameObject object : gameObjects) {
                action.doIt(object);
            }
        }
    }

    public interface GameObjectHandler {
        void doIt(GameObject target);
    }

    private boolean collision(GameObject object1, GameObject object2) {
        return
                ((object1.getPosition().x - object2.getPosition().x ))
                        * ((object1.getPosition().x ) - (object2.getPosition().x ))
                        + ((object1.getPosition().y ) - (object2.getPosition().y ))
                        * ((object1.getPosition().y ) - (object2.getPosition().y ))
                        < object1.getCollisionRadius() + object2.getCollisionRadius();
    }

    public SpaceSnapshot makeSnapshot() {
        synchronized (monitor) {
            List<SpaceSnapshot.GameObjectSnapshot> gameObjectSnapshots = new ArrayList<SpaceSnapshot.GameObjectSnapshot>();

            for (GameObject object : gameObjects) {
                gameObjectSnapshots.add(object.snapshot());
            }

            return new SpaceSnapshot("Bla", 1235, gameObjectSnapshots);
        }
    }

    public void elapseTime(final float delta) {
        synchronized (monitor) {
            Set<GameObject> toDelete = new HashSet<GameObject>();
            Set<GameObject> toAdd = new HashSet<GameObject>();

            for (GameObject target : gameObjects) {
                target.elapseTime(delta, toDelete, toAdd);
            }

//TODO: signalOutOfBounds() bounces the target up and down (y-axis), but not left and right (x-axis)...fix it!
            for (GameObject target : gameObjects) {
                if (!toDelete.contains(target)) {
                    if (target.getPosition().x < minX || target.getPosition().x - minX > width ||
                            target.getPosition().y < minY || target.getPosition().y - minY > height) {
                        target.signalOutOfBounds(toDelete, toAdd);
                    }
                }
            }
/**
 * See if any gameobjects collide with each other
 */
            for (GameObject target : gameObjects) {
                if (!toDelete.contains(target)) {
                    for (GameObject subject : gameObjects) {
                        if (!toDelete.contains(subject)) {
                            if (target != subject) {
                                if (collision(target, subject)) {
                                    target.collide(subject, toDelete, toAdd);
                                }
                            }
                        }
                    }
                }
            }


/**
 * In this section gameobjects calculate how far they are of each other and they attack in their different ways
 */

            for(GameObject subject: gameObjects){
                if(subject instanceof Enemy){
                    for(GameObject target: gameObjects){
                        if(target instanceof Player){
                            ((Enemy) subject).getNearestTarget(target,target,toDelete, toAdd);
                            subject.targetSpotted(target, toDelete, toAdd);
                            subject.attackTarget(target, toDelete, toAdd);
                            subject.nothingSpotted(target, toDelete, toAdd);
                        }
                    }
                }
            }


            for (GameObject gameObject : toDelete) {
                gameObjects.remove(gameObject);
            }

            for (GameObject gameObject : toAdd) {
                gameObjects.add(gameObject);
            }
        }
    }
}

package com.wisekrakr.firstgame.engine;

import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

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
                (object1.getPosition().x - object2.getPosition().x) * (object1.getPosition().x - object2.getPosition().x) +
                (object1.getPosition().y - object2.getPosition().y) * (object1.getPosition().y - object2.getPosition().y) < object1.getCollisionRadius() + object2.getCollisionRadius();
    }

    public void elapseTime(final float delta) {
        synchronized (monitor) {
            for (GameObject target : gameObjects) {
                target.elapseTime(delta);
            }

            Set<GameObject> toDelete = new HashSet<GameObject>();
            Set<GameObject> toAdd = new HashSet<GameObject>();

            for (GameObject target : gameObjects) {
                if (target.getPosition().x < minX || target.getPosition().x - minX > width ||
                        target.getPosition().y < minY || target.getPosition().y - minY > height) {
                    target.signalOutOfBounds(toDelete, toAdd);
                }
            }

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

            for (GameObject gameObject: toDelete) {
                gameObjects.remove(gameObject);
            }

            for (GameObject gameObject: toAdd) {
                gameObjects.add(gameObject);
            }
        }
    }
}

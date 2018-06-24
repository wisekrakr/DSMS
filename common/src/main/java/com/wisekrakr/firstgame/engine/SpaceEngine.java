package com.wisekrakr.firstgame.engine;

import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;

import java.util.*;

public class SpaceEngine {
    private final Object monitor = new Object();
    private final float minX;
    private final float minY;
    private final float width;
    private final float height;
    private Set<GameObject> gameObjects = new HashSet<GameObject>();
    private Map<GameObject, GameObjectListener> listeners = new HashMap<>();
    private float clock = 0f;
    private boolean paused = false;

    public SpaceEngine(float minX, float minY, float width, float height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    public interface GameObjectListener {
        void added();
        void removed();
    }

    public void addGameObject(GameObject object) {
        addGameObject(object, null);
    }

    public void addGameObject(GameObject object, GameObjectListener listener) {
        synchronized (monitor) {
            if (!gameObjects.add(object)) {
                throw new IllegalArgumentException("Game object already present");
            }

            if (listener != null) {
                listeners.put(object, listener);
                listener.added();
            }

            List<GameObject> toAdd = new ArrayList<>();
            List<GameObject> toRemove = new ArrayList<>();
            object.afterAdd(toAdd, toRemove);

            for (GameObject o: toAdd) {
                addGameObject(o);
            }
            for (GameObject o: toRemove) {
                removeGameObject(o);
            }
        }
    }

    public void removeGameObject(GameObject object) {
        synchronized (monitor) {
            if (!gameObjects.remove(object)) {
                throw new IllegalArgumentException("Game object not present");
            }

            GameObjectListener listener = listeners.remove(object);
            if (listener != null) {
                listeners.put(object, listener);
                listener.removed();
            }

            List<GameObject> toAdd = new ArrayList<>();
            List<GameObject> toRemove = new ArrayList<>();
            object.afterRemove(toAdd, toRemove);

            for (GameObject o: toAdd) {
                addGameObject(o);
            }
            for (GameObject o: toRemove) {
                removeGameObject(o);
            }
        }
    }

    public void pause() {
        synchronized (monitor) {
            paused = true;
        }
    }

    public void resume() {
        synchronized (monitor) {
            paused = false;
        }
    }

    public void togglePause() {
        synchronized (monitor) {
            paused = !paused;
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
                Math.sqrt(
                        (((object1.getPosition().x) - (object2.getPosition().x)))
                                * ((object1.getPosition().x) - (object2.getPosition().x))
                                + ((object1.getPosition().y) - (object2.getPosition().y))
                                * ((object1.getPosition().y) - (object2.getPosition().y)))
                        < (object1.getCollisionRadius() + object2.getCollisionRadius());
    }

    private void nearestTarget(GameObject subject, Set<GameObject> targets, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        for (GameObject target : targets) {
            if (Math.abs(target.getPosition().x - subject.getPosition().x) < Math.abs(target.getPosition().x - subject.getPosition().x)) {
                if (Math.abs(target.getPosition().y - subject.getPosition().y) < Math.abs(target.getPosition().y - subject.getPosition().y)) {
                    subject.attackTarget(target, toDelete, toAdd);
                }
            }
        }
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

    public float getTime() {
        return clock;
    }

    public void elapseTime(float delta) {
        synchronized (monitor) {
            if (paused) {
                delta = 0F;
            }

            clock = clock + delta;
            Set<GameObject> toDelete = new HashSet<GameObject>();
            Set<GameObject> toAdd = new HashSet<GameObject>();

            for (GameObject target : gameObjects) {
                target.elapseTime(clock, delta, toDelete, toAdd);
            }

            for (GameObject target : gameObjects) {
                if (!toDelete.contains(target)) {
                    if (target.getPosition().x < minX || target.getPosition().x - minX > width ||
                            target.getPosition().y < minY || target.getPosition().y - minY > height) {
                        target.signalOutOfBounds(toDelete, toAdd);
                    }
                }
            }
/**
 * See if any gameobjects collide with each other, than proceed with the collide method of GameObject extended classes
 */
            for (GameObject target : gameObjects) {
                if (!toDelete.contains(target)) {
                    for (GameObject subject : gameObjects) {
                        if (!toDelete.contains(subject)) {
                            if (target != subject) {
                                if (collision(target, subject)) {
                                    target.collide(subject, toDelete, toAdd);
                                    target.overlappingObjects(subject, toDelete, toAdd);
                                }
                            }
                        }
                    }
                }
            }


/**
 * In this section gameobjects(enemy package) calculate how far they are of each other and they attack in their different ways
 */

            for (GameObject subject : gameObjects) {
                if (subject instanceof Enemy) {
                    for (GameObject target : gameObjects) {
                        if (target instanceof Spaceship) {
                            //subject.getClosestTarget(target, toDelete, toAdd);
                            if (target != subject) {
                                subject.targetSpotted(target, toDelete, toAdd);
                                subject.attackTarget(target, toDelete, toAdd);
                            }
                        }
                    }
                }
            }
/**
 * In this section gameobjects(enemy weaponry ) are Enemy weapons that follow the player(homing weapons).
 */

            for (GameObject subject : gameObjects) {
                if (subject instanceof Spores || subject instanceof HomingMissile) {
                    for (GameObject target : gameObjects) {
                        if (target instanceof Spaceship) {
                            if (target != subject ) {
                                subject.attackTarget(target, toDelete, toAdd);
                            }
                        }
/**
 * In this section gameobjects( player weaponry ) calculate how far they are of each other and they attack in their different ways
 */
                        if (target instanceof Enemy) {
                            if (target != subject) {
                                subject.attackTarget(target,toDelete,toAdd);
                            }
                        }
                    }
                }
            }


/**
 * In this section gameobjects( minions of Player ) calculate how far they are of each other and they attack in their different ways
 */
            for (GameObject subject : gameObjects) {
                if (subject instanceof Minion) {
                    for (GameObject target : gameObjects) {
                        if (target instanceof Enemy) {
                            if (target != subject) {
                                subject.getClosestTarget(target, toDelete, toAdd);
                                subject.attackTarget(target, toDelete, toAdd);
                            }
                        }
/**
 * In this section gameobjects( minions of Enemy ) calculate how far they are of each other and they attack in their different ways
 */
                        if (target instanceof Player) {
                            if (target != subject) {
                                subject.getClosestTarget(target, toDelete, toAdd);
                                subject.attackTarget(target, toDelete, toAdd);
                            }
                        }
                    }
                }
            }

/**
 * Scoring system
 */

            for (GameObject player : gameObjects) {
                if (player instanceof Player) {
                    for (GameObject enemy : gameObjects) {
                        if (enemy instanceof Enemy) {
                            for(GameObject subject: gameObjects){
                                if(subject instanceof Bullet){
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                                if(subject instanceof HomingMissile){
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                                if(subject instanceof SpaceMine){
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                            }
                        }
                    }
                }
            }


            for (GameObject gameObject : toDelete) {
                removeGameObject(gameObject);
            }

            for (GameObject gameObject : toAdd) {
                addGameObject(gameObject);
            }
        }
    }
}

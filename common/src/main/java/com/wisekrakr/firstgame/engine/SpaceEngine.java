package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractGameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.BulletObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.MissileObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.*;
import com.wisekrakr.firstgame.engine.physicalobjects.*;

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

    private Set<PhysicalObjectRunner> physicalObjects = new HashSet<>();

    public SpaceEngine(float minX, float minY, float width, float height) {
        this.minX = minX;
        this.minY = minY;
        this.width = width;
        this.height = height;
    }

    public PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener listener) {
        PhysicalObjectRunner result = new PhysicalObjectRunner(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, Collections.emptyMap(), collisionRadius, listener);

        physicalObjects.add(result);

        return result;
    }

    public void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualizationEngine, Float collisionRadius) {
        PhysicalObjectRunner runner = getPhysicalObject(target);

        runner.update(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius);

    }

    private PhysicalObjectRunner getPhysicalObject(PhysicalObject target) {
        if (target instanceof PhysicalObjectRunner && physicalObjects.contains(target)) {
            return (PhysicalObjectRunner) target;
        }

        throw new IllegalArgumentException("Unknown physical object: " + target.getName());
    }

    public void updatePhysicalObjectExtra(PhysicalObject target, String key, Object value) {
        getPhysicalObject(target).updateExtra(key, value);
    }

    public void removePhysicalObject(PhysicalObject object) {
        if (!physicalObjects.remove(object)) {
            throw new IllegalArgumentException("Unknown physical object:" + object);
        }
    }

    public interface GameObjectListener {
        void added();

        void removed();
    }

    public GameObject addGameObject(GameObject object) {
        return addGameObject(object, null);
    }

    public GameObject addGameObject(GameObject object, GameObjectListener listener) {
        synchronized (monitor) {
            if (!gameObjects.add(object)) {
                throw new IllegalArgumentException("Game object already present: " + object);
            }

            if (listener != null) {
                listeners.put(object, listener);
                listener.added();
            }

            List<GameObject> toAdd = new ArrayList<>();
            List<GameObject> toRemove = new ArrayList<>();
            object.afterAdd(toAdd, toRemove);

            for (GameObject o : toAdd) {
                addGameObject(o);
            }
            for (GameObject o : toRemove) {
                removeGameObject(o);
            }
        }

        return object;
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

            for (GameObject o : toAdd) {
                addGameObject(o);
            }
            for (GameObject o : toRemove) {
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

    private Collision collisionDetection(PhysicalObject object1, PhysicalObject object2) {
        if (Math.sqrt(
                (((object1.getPosition().x) - (object2.getPosition().x)))
                        * ((object1.getPosition().x) - (object2.getPosition().x))
                        + ((object1.getPosition().y) - (object2.getPosition().y))
                        * ((object1.getPosition().y) - (object2.getPosition().y)))
                < (object1.getCollisionRadius() + object2.getCollisionRadius())) {

            float epicenterX = (object1.getCollisionRadius() * object1.getPosition().x + object2.getCollisionRadius() * object2.getPosition().x) / (
                    object1.getCollisionRadius() + object2.getCollisionRadius()
            );

            float epicenterY = (object1.getCollisionRadius() * object1.getPosition().y + object2.getCollisionRadius() * object2.getPosition().y) / (
                    object1.getCollisionRadius() + object2.getCollisionRadius()
            );

            // TODO: implement impact
            return new Collision(object1, object2, new Vector2(epicenterX, epicenterY), clock, 1);
        } else {
            return null;
        }
    }

    private NearestPhysicalObject nearestPhysicalObject(PhysicalObject subject, PhysicalObject target){
        //TODO: Change distance value

        if (GameHelper.distanceBetweenPhysicals(subject, target) < 500) {
            return new NearestPhysicalObject(subject, target, clock);
        } else {
            return null;
        }


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

    public SpaceSnapshot makeSnapshot() {
        synchronized (monitor) {
            List<SpaceSnapshot.GameObjectSnapshot> gameObjectSnapshots = new ArrayList<>();

            for (GameObject object : gameObjects) {
                gameObjectSnapshots.add(object.snapshot());
            }

            List<PhysicalObjectSnapshot> physicalObjectSnapshots = new ArrayList<>();

            for (PhysicalObjectRunner object : physicalObjects) {
                physicalObjectSnapshots.add(object.snapshot());
            }

            return new SpaceSnapshot("Bla", clock, gameObjectSnapshots, physicalObjectSnapshots);
        }
    }

    public float getTime() {
        return clock;
    }

    private void physicalElapseTime(float delta) {
        // apply physics:
        //    A. apply movement
        for (PhysicalObjectRunner target : physicalObjects) {

            float speedX = (float) Math.cos(target.getSpeedDirection()) * target.getSpeedMagnitude() * delta;
            float speedY = (float) Math.sin(target.getSpeedDirection()) * target.getSpeedMagnitude() * delta;

            target.update(
                    null,
                    new Vector2(target.getPosition().x + speedX, target.getPosition().y + speedY),
                    null,
                    null,
                    null,
                    null,
                    null);

            // TODO: distanceTravelled = distanceTravelled + Math.abs(target.getSpeedMagnitude() * delta);    ?
        }

        //    B. detect collisions

        List<Collision> collisions = new ArrayList<>();
        Set<PhysicalObjectRunner> processed = new HashSet<>();

        for (PhysicalObjectRunner target : physicalObjects) {
            processed.add(target);
            for (PhysicalObjectRunner subject : physicalObjects) {
                if (!processed.contains(subject)) {
                    Collision collision = collisionDetection(target, subject);

                    if (collision != null) {
                        collisions.add(collision);
                    }
                }
            }
        }

        //    C. report to the owners

        for (Collision collision : collisions) {
            getPhysicalObject(collision.getOne()).getListener().collision(collision.getTwo(), collision.getTime(), collision.getEpicentre(), collision.getImpact());
            getPhysicalObject(collision.getTwo()).getListener().collision(collision.getOne(), collision.getTime(), collision.getEpicentre(), collision.getImpact());
        }

        //    D.  out of bounds

        for (PhysicalObjectRunner target : physicalObjects) {

            if (target.getPosition().x < minX || target.getPosition().x - minX > width ||
                    target.getPosition().y < minY || target.getPosition().y - minY > height) {
                target.signalOutOfBounds();
            }
        }

        //    E.  recognize other physicalObjects
        List<NearestPhysicalObject> nearby = new ArrayList<>();
        Set<PhysicalObjectRunner> targeted = new HashSet<>();
        for (PhysicalObjectRunner target : physicalObjects) {
            targeted.add(target);
            for (PhysicalObjectRunner subject : physicalObjects) {
                if (!targeted.contains(subject)) {
                    NearestPhysicalObject nearestPhysicalObject = nearestPhysicalObject(subject, target);

                    if (nearestPhysicalObject != null) {
                        nearby.add(nearestPhysicalObject);
                    }
                }
            }
        }

        for (NearestPhysicalObject nearestPhysicalObject: nearby){
            getPhysicalObject(nearestPhysicalObject.getOne()).getListener().nearby(nearestPhysicalObject.getTwo(), nearestPhysicalObject.getTime(), nearestPhysicalObject.getTwo().getPosition());
            getPhysicalObject(nearestPhysicalObject.getTwo()).getListener().nearby(nearestPhysicalObject.getOne(), nearestPhysicalObject.getTime(), nearestPhysicalObject.getOne().getPosition());
        }
    }

    public void elapseTime(float delta) {
        synchronized (monitor) {
            if (paused) {
                delta = 0F;
            }

            clock = clock + delta;

            physicalElapseTime(delta);

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


            for (GameObject subject : gameObjects) {
                // TODO: change into universal behavior
                if (subject instanceof NonPlayerCharacter || subject instanceof Player) {
                    List<GameObject> nearby = new ArrayList<>();

                    for (GameObject target : gameObjects) {
                        if (target != subject && GameHelper.distanceBetween(target, subject) < subject.getActionDistance()) {
                            nearby.add(target);
                        }
                    }
                    subject.nearby(nearby);
                }
            }


/**
 * In this section gameobjects(enemy weaponry ) are Enemy weapons that follow the player(homing weapons).
 */

            for (GameObject subject : gameObjects) {
                if (subject instanceof Spores || subject instanceof HomingMissile) {
                    for (GameObject target : gameObjects) {
                        if (target instanceof Spaceship) {
                            if (target != subject) {
                                subject.attackTarget(target, toDelete, toAdd);
                            }
                        }
/**
 * In this section gameobjects( player weaponry ) calculate how far they are of each other and they attack in their different ways
 */
                        if (target instanceof Enemy || target instanceof NonPlayerCharacter) {
                            if (target != subject) {
                                subject.attackTarget(target, toDelete, toAdd);
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
                        if (enemy instanceof NonPlayerCharacter) {
                            for (GameObject subject : gameObjects) {
                                if (subject instanceof BulletObject) {
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                                if (subject instanceof MissileObject) {
                                    ((Player) player).scoringSystem(enemy, subject);
                                }
                                if (subject instanceof SpaceMine) {
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

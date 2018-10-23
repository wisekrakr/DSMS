package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.physicalobjects.*;

import java.util.*;

public class SpaceEngine {
    private final Object monitor = new Object();

    private Set<GameObject> gameObjects = new HashSet<GameObject>();
    private Map<GameObject, GameObjectListener> listeners = new HashMap<>();
    private float clock = 0f;
    private boolean paused = false;

    //TODO: get rid
    private World world = new World(new Vector2(0, 0), true);
    private Set<Body> bodies = new HashSet<>();

    private Set<PhysicalObjectRunner> physicalObjects = new HashSet<>();


    public PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, float health, float damage, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener listener) {
        synchronized (monitor) {
            PhysicalObjectRunner result = new PhysicalObjectRunner(name, position, orientation, speedMagnitude, speedDirection, health, damage, visualizationEngine, Collections.emptyMap(), collisionRadius, listener);

            physicalObjects.add(result);

            return result;
        }
    }

    public void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualizationEngine, Float collisionRadius, Float health, Float damage) {
        synchronized (monitor) {
            PhysicalObjectRunner runner = getPhysicalObject(target);

            runner.update(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius, health, damage);
        }

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

        ((PhysicalObjectRunner) object).getListener().removed(object);
    }

    public List<NearPhysicalObject> findNearbyPhysicalObjects(PhysicalObject reference, float maxDistance) {
        List<NearPhysicalObject> result = new ArrayList<>();

        for (PhysicalObjectRunner target : physicalObjects) {
            if (target != reference) {
                float distance = GameHelper.distanceBetweenPhysicals(reference, target);
                if (distance < maxDistance) {
                    result.add(new NearPhysicalObject(target, distance));
                }
            }
        }

        Collections.sort(result, (o1, o2) -> Float.compare(o1.getDistance(), o2.getDistance()));

        return result;
    }

    public Body addDynamicBody(float density, float friction, float restitution) {

        Body shipBody = null;
        for (PhysicalObjectRunner target : physicalObjects) {
            Number radiusRaw = (Number) target.getExtraProperties().get("radius");
            if (radiusRaw == null) {
                radiusRaw = 5f;
            }
            float radius = radiusRaw.floatValue();

            BodyDef shipBodyDef = new BodyDef();
            shipBodyDef.type = BodyDef.BodyType.DynamicBody;
            shipBodyDef.position.set(target.getPosition().x, target.getPosition().y);
            shipBodyDef.angle = target.getOrientation();

            shipBody = getWorld().createBody(shipBodyDef);
            bodies.add(shipBody);

            CircleShape shipCircleShape = new CircleShape();
            shipCircleShape.setRadius(radius);

            FixtureDef shipFixtureDef = new FixtureDef();
            shipFixtureDef.shape = shipCircleShape;
            shipFixtureDef.density = density;
            shipFixtureDef.friction = friction;
            shipFixtureDef.restitution = restitution;

            shipBody.createFixture(shipFixtureDef);
        }
        return shipBody;
    }

    public interface GameObjectListener {
        void added();

        void removed();
    }

    @Deprecated
    public GameObject addGameObject(GameObject object) {
        return addGameObject(object, null);
    }

    @Deprecated
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

    @Deprecated
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

    public World getWorld() {

        return world;
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
                    null,
                    null,
                    null
            );

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
            getPhysicalObject(collision.getOne()).getListener().collision(collision.getOne(), collision.getTwo(), collision.getTime(), collision.getEpicentre(), collision.getImpact());
            getPhysicalObject(collision.getTwo()).getListener().collision(collision.getTwo(), collision.getOne(), collision.getTime(), collision.getEpicentre(), collision.getImpact());
        }

        //    D.  Dynamic Box2d bodies

        for (Body body : bodies) {
            while (body.getFixtureList().size > 0) {
                body.destroyFixture(body.getFixtureList().first());
            }
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

            /*
            for (GameObject target : gameObjects) {
                if (!toDelete.contains(target)) {
                    if (target.getPosition().x < minX || target.getPosition().x - minX > width ||
                            target.getPosition().y < minY || target.getPosition().y - minY > height) {
                        target.signalOutOfBounds(toDelete, toAdd);
                    }
                }
            }
            */
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


            for (GameObject subject : gameObjects) {
                // TODO: change into universal behavior
                if (subject instanceof NonPlayerCharacter) {
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
 * Scoring system
 */
/*
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

*/
            for (GameObject gameObject : toDelete) {
                removeGameObject(gameObject);
            }

            for (GameObject gameObject : toAdd) {
                addGameObject(gameObject);
            }
        }
    }
}

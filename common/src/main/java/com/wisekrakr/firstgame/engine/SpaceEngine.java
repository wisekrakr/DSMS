package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractNPCTools;
import com.wisekrakr.firstgame.engine.gamecharacters.CollisionModel;
import com.wisekrakr.firstgame.engine.physicalobjects.*;

import java.util.*;

public class SpaceEngine {
    private float visibleRadius = 1000f;
    private float creationRadius = 2000f;
    private float vitalizationRadius = 3000f;
    private float slowUpdateInterval = 10;
    private float stasisPeriod = 0f;

    private final Object monitor = new Object();
    private float clock = 0f;
    private float lastSlowUpdate = 0f;
    private boolean paused = false;

    private Set<PhysicalObjectRunner> physicalObjects = new HashSet<>();

    private List<PhysicalObjectRunner> vitalizingObjects = new ArrayList<>();

    public void markVitalizer(PhysicalObject physicalObject) {
        synchronized (monitor) {
            vitalizingObjects.add(getPhysicalObject(physicalObject));
        }
    }


    public PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectEvictionPolicy evictionPolicy, PhysicalObjectListener listener) {
        synchronized (monitor) {
            PhysicalObjectRunner result = new PhysicalObjectRunner(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, Collections.emptyMap(), collisionRadius, evictionPolicy, listener);

            physicalObjects.add(result);

            return result;
        }
    }

    public void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualizationEngine, Float collisionRadius) {
        synchronized (monitor) {
            PhysicalObjectRunner runner = getPhysicalObject(target);

            runner.update(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius);
        }

    }

    public AbstractNPCTools getNPCTools(){
        synchronized (monitor){

            return new AbstractNPCTools();
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

    public void tagPhysicalObject(PhysicalObject target, String tag) {
        getPhysicalObject(target).tag(tag);

    }

    public void untagPhysicalObject(PhysicalObject target, String tag) {
        getPhysicalObject(target).untag(tag);
    }

    public float getCreationAreaSize() {
        synchronized (monitor) {
            // TODO: ignores overlap
            return vitalizingObjects.size() * ((float) Math.PI * ((creationRadius * creationRadius) - (visibleRadius * visibleRadius)));
        }
    }

    public float getVitalAreaSize() {
        synchronized (monitor) {
            // TODO: ignores overlap
            return vitalizingObjects.size() * ((float) Math.PI * ((vitalizationRadius * vitalizationRadius)));
        }
    }

    public Vector2 chooseCreationPoint() {
        synchronized (monitor) {
            if (vitalizingObjects.size() > 0) {
                Vector2 candidate;

                int maxTries = 10;
                do {
                    PhysicalObjectRunner centerObject = vitalizingObjects.get(GameHelper.randomGenerator.nextInt(vitalizingObjects.size()));

                    float angle = GameHelper.randomDirection();
                    float magnitude = GameHelper.generateRandomNumberBetween(visibleRadius, creationRadius);

                    candidate = GameHelper.applyMovement(centerObject.getPosition(), angle, magnitude);
                } while (isVisible(candidate) && maxTries-- > 0);

                // TODO: ignores overlap
                return candidate;
            }

            return null;
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

            float impact = CollisionModel.calculateDamage(object1, object2);



            // TODO: implement impact
            return new Collision(object1, object2, new Vector2(epicenterX, epicenterY), clock, impact);
        } else {
            return null;
        }
    }



    //TODO: create an reaction class like Collision above and getTools to find out who to attack


    public SpaceSnapshot makeSnapshot() {
        synchronized (monitor) {

            List<PhysicalObjectSnapshot> physicalObjectSnapshots = new ArrayList<>();

            for (PhysicalObjectRunner object : physicalObjects) {
                physicalObjectSnapshots.add(object.snapshot());
            }

            return new SpaceSnapshot("Bla", clock, physicalObjectSnapshots);
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

            boolean updateSlow = (clock - lastSlowUpdate) > slowUpdateInterval;

            List<PhysicalObjectRunner> discarded = new ArrayList<>();

            // apply physics:
            //    A. apply movement
            for (PhysicalObjectRunner target : physicalObjects) {
                boolean vital = isVital(target.getPosition());

                if (vital || (target.getPolicy() == PhysicalObjectEvictionPolicy.SLOW && updateSlow)) {
                    float actualDelta = vital ? delta : (clock - lastSlowUpdate);

                    target.update(
                            null,
                            GameHelper.applyMovement(target.getPosition(), target.getSpeedDirection(), target.getSpeedMagnitude() * actualDelta),
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                }

                if (!vital && target.getPolicy() == PhysicalObjectEvictionPolicy.DISCARD) {
                    discarded.add(target);
                }

                if (!vital && target.getPolicy() == PhysicalObjectEvictionPolicy.STASIS) {
                    stasisPeriod += delta;
                    if (!(stasisPeriod > 30f)) {
                        target.update(null,
                                null,
                                null,
                                0f,
                                null,
                                null,
                                null
                        );
                    }else {
                        discarded.add(target);
                        stasisPeriod = 0f;
                    }
                }

                // TODO: distanceTravelled = distanceTravelled + Math.abs(target.getSpeedMagnitude() * delta);    ?
            }

            for (PhysicalObjectRunner runner : discarded) {
                removePhysicalObject(runner);
            }

            if (updateSlow) {
                lastSlowUpdate = clock;
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
        }
    }

    private boolean isVital(Vector2 position) {
        for (PhysicalObject vital : vitalizingObjects) {
            if (GameHelper.distanceBetween(position, vital.getPosition()) < vitalizationRadius) {
                return true;
            }
        }

        return false;
    }

    private boolean isVisible(Vector2 position) {
        for (PhysicalObject vital : vitalizingObjects) {
            if (GameHelper.distanceBetween(position, vital.getPosition()) < visibleRadius) {
                return true;
            }
        }

        return false;
    }
}

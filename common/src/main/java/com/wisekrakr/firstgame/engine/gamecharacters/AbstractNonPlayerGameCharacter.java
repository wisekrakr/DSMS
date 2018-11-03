package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.Behavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.BehaviorContext;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectEvictionPolicy;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.*;

public class AbstractNonPlayerGameCharacter extends AbstractGameCharacter {
    private Map<PhysicalObject, List<Behavior>> behavedObjects = new HashMap<>();
    private Set<PhysicalObject> deleted = new HashSet<>();

    interface BehavedObject {
        void behave(List<Behavior> behaviors);

        PhysicalObject getObject();
    }

    interface BehavedObjectListener {
        void removed();
    }

    protected final BehavedObject introduceBehavedObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, BehavedObjectListener listener) {
        PhysicalObject subject = getContext().addPhysicalObject(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius, new PhysicalObjectListener() {
            @Override
            public void collision(PhysicalObject myself, PhysicalObject two, float time, Vector2 epicentre, float impact) {
                List<Behavior> behaviors = behavedObjects.get(myself);

                for (Behavior behavior : behaviors) {
                    if (!deleted.contains(myself)) {
                        behavior.collide(two, epicentre, impact);
                    }
                }
            }

            @Override
            public void removed(PhysicalObject target) {
                List<Behavior> behaviors = behavedObjects.remove(target);

                if (behaviors != null) {
                    for (Behavior behavior : behaviors) {
                        removedBehavior(behavior);
                    }
                }

                if (listener != null) {
                    listener.removed();
                }
            }
        }, PhysicalObjectEvictionPolicy.DISCARD);

        behavedObjects.put(subject, null);

        return new BehavedObject() {
            @Override
            public void behave(List<Behavior> behaviors) {
                if (behavedObjects.containsKey(subject)) {
                    List<Behavior> old = behavedObjects.put(subject, behaviors);
                    if (old != null) {
                        for (Behavior o : old) {
                            removedBehavior(o);
                        }
                    }

                    if (behaviors != null) {
                        for (Behavior n : behaviors) {
                            addedBehavior(subject, n);

                        }
                    }
                }
            }

            @Override
            public PhysicalObject getObject() {
                PhysicalObject object = null;
                if (behavedObjects.containsKey(subject)) {
                    object = subject;
                }
                return object;
            }
        };
    }

    private void removedBehavior(Behavior b) {
        b.stop();
    }

    private void addedBehavior(PhysicalObject subject, Behavior b) {
        b.init(new BehaviorContext() {
            @Override
            public void addCharacter(GameCharacter newObject, GameCharacterListener listener) {
                getContext().addCharacter(newObject, listener);
            }

            @Override
            public void updatePhysicalObject(String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualizationEngine, Float collisionRadius) {
                AbstractNonPlayerGameCharacter.this.getContext().updatePhysicalObject(subject, name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius);
            }

            @Override
            public void updatePhysicalObjectExtra(String key, Object value) {
                AbstractNonPlayerGameCharacter.this.getContext().updatePhysicalObjectExtra(subject, key, value);
            }


            @Override
            public void removePhysicalObject() {
                deleted.add(subject);
            }

            @Override
            public PhysicalObject getSubject() {
                return subject;
            }

        });

        b.start();
    }


    @Override
    public final void elapseTime(float delta) {
        for (Map.Entry<PhysicalObject, List<Behavior>> entry : behavedObjects.entrySet()) {
            if (!deleted.contains(entry.getKey())) {
                for (Behavior b : entry.getValue()) {
                    b.elapseTime(getContext().getSpaceEngine().getTime(), delta);

                    if (deleted.contains(entry.getKey())) {
                        break;
                    }
                }
            }
        }

        for (PhysicalObject object : deleted) {
            getContext().removePhysicalObject(object);
        }

        deleted.clear();


    }

}

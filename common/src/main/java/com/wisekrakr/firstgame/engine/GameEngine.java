package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterListener;
import com.wisekrakr.firstgame.engine.physicalobjects.*;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameEngine {
    private SpaceEngine space;
    private float previousUpdate = -1f;
    private float updateFrequency = 0.1f;

    private List<Scenario> scenarios = new ArrayList<>();

    private Map<GameCharacter, GameCharacterRunner> characters = new ConcurrentHashMap<>();

    private Set<GameCharacter> deleted = new HashSet<>();
    private Set<Scenario> deletedScenarios = new HashSet<>();

    public GameEngine(SpaceEngine space) {
        this.space = space;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);

        scenario.init(new Scenario.ScenarioContext() {
            @Override
            public SpaceEngine space() {
                return space;
            }

            @Override
            public GameEngine engine() {
                return GameEngine.this;
            }

            @Override
            public void removeMyself() {
                // TODO: scenarios are never removed
            }
        });

        scenario.start();
    }

    private void removeScenario(Scenario scenario) {
        if (scenarios.remove(scenario)) {
            scenario.stop();
        }
    }

    private void removeGameCharacter(GameCharacter character) {
        GameCharacterRunner runner = characters.remove(character);
        if (runner != null) {
            character.stop();

            Iterator<PhysicalObject> iterator = runner.physicalObjects.iterator();
            while (iterator.hasNext()) {
                PhysicalObject p = iterator.next();
                if (runner.physicalObjects.contains(p)) {
                    iterator.remove();
                    space.removePhysicalObject(p);
                }
            }

            if (runner.listener != null) {
                runner.listener.removed(character);
            }
        }
    }

    private class GameCharacterRunner {
        private GameCharacter character;
        private GameCharacterListener listener;
        private Set<PhysicalObject> physicalObjects = new HashSet<>();

        public GameCharacterRunner(GameCharacter character, GameCharacterListener listener) {
            this.character = character;
            this.listener = listener;
        }

        public GameCharacter getCharacter() {
            return character;
        }

        public Set<PhysicalObject> getPhysicalObjects() {
            return physicalObjects;
        }
    }

    public void addGameCharacter(GameCharacter character, GameCharacterListener listener) {
        if (characters.containsKey(character)) {
            throw new IllegalArgumentException("Already have character " + character);
        }

        GameCharacterRunner runner = new GameCharacterRunner(character, listener);

        character.init(new GameCharacterContext() {
            @Override
            public SpaceEngine getSpaceEngine() {
                return space;
            }

            @Override
            public void addCharacter(GameCharacter newObject, GameCharacterListener listener) {
                GameEngine.this.addGameCharacter(newObject, listener);
            }

            @Override
            public PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener listener, PhysicalObjectEvictionPolicy evictionPolicy) {
                PhysicalObject result = space.addPhysicalObject(name, position, orientation, speedMagnitude, speedDirection,visualizationEngine, collisionRadius, evictionPolicy, new PhysicalObjectListener() {
                    @Override
                    public void collision(PhysicalObject myself, PhysicalObject two, float time, Vector2 epicentre, float impact) {
                        if (listener != null) {
                            listener.collision(myself, two, time, epicentre, impact);
                        }
                    }

                    @Override
                    public void removed(PhysicalObject target) {
                        runner.getPhysicalObjects().remove(target);

                        if (listener != null) {
                            listener.removed(target);
                        }
                    }
                });

                runner.getPhysicalObjects().add(result);

                return result;
            }

            private void assureMine(PhysicalObject target) {
                if (!runner.getPhysicalObjects().contains(target)) {
                    throw new IllegalArgumentException("Not my physical object");
                }
            }

            @Override
            public void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualizationEngine, Float collisionRadius) {
                assureMine(target);

                space.updatePhysicalObject(target, name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius);
            }

            @Override
            public void updatePhysicalObjectExtra(PhysicalObject target, String key, Object value) {
                assureMine(target);

                space.updatePhysicalObjectExtra(target, key, value);
            }

            @Override
            public void tagPhysicalObject(PhysicalObject target, String tag) {
                assureMine(target);

                space.tagPhysicalObject(target, tag);
            }

            @Override
            public void untagPhysicalObject(PhysicalObject target, String tag) {
                assureMine(target);

                space.untagPhysicalObject(target, tag);

            }

            @Override
            public void removePhysicalObject(PhysicalObject object) {
                assureMine(object);

                space.removePhysicalObject(object);
            }

            @Override
            public void removeMyself() {
                deleted.add(character);
            }

            @Override
            public PhysicalObject getPhysicalObject() {
                PhysicalObject object = null;
                Iterator<PhysicalObject> iterator = runner.physicalObjects.iterator();
                while (iterator.hasNext()) {
                    PhysicalObject p = iterator.next();
                    if (runner.physicalObjects.contains(p)) {
                        object = p;
                    }
                }
                return object;
            }

            @Override
            public List<NearPhysicalObject> findNearbyPhysicalObjects(PhysicalObject reference, float radius) {
                return space.findNearbyPhysicalObjects(reference, radius);

            }

        });

        characters.put(character, runner);
        character.start();
    }

    /*
    public void start() {
    }
*/
    public void elapseTime(float delta) {
        space.elapseTime(delta);

        for (GameCharacter c : characters.keySet()) {
            try {
                c.elapseTime(delta);
            }
            catch (Exception e) {
                System.out.println("Misbehaving game character " + c + ": " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }

        for (GameCharacter d : deleted) {
            removeGameCharacter(d);
        }

        if (space.getTime() > previousUpdate + updateFrequency) {
            previousUpdate = space.getTime();

            periodicUpdate();
        }

        deleted.clear();
        for (Scenario d : deletedScenarios) {
            removeScenario(d);
        }
        deletedScenarios.clear();
    }

    private void periodicUpdate() {
        for (Scenario scenario : scenarios) {
            scenario.periodicUpdate();
        }
    }
}
package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.physicalobjects.*;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;

import java.util.*;

public class GameEngine {
    private SpaceEngine space;
    private float previousUpdate = -1f;
    private float updateFrequency = 0.1f;

    private List<Scenario> scenarios = new ArrayList<>();

    private Map<GameCharacter, GameCharacterRunner> characters = new HashMap<>();

    private Set<GameCharacter> deleted = new HashSet<>();

    public GameEngine(SpaceEngine space) {
        this.space = space;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);

        scenario.initialUpdate(space);
    }

    private void removeGameCharacter(GameCharacter character) {
        GameCharacterRunner runner = characters.remove(character);
        if (runner != null) {
            character.stop();

            for (PhysicalObject object : runner.getPhysicalObjects()) {
                space.removePhysicalObject(object);
            }
        }
    }

    private class GameCharacterRunner {
        private GameCharacter character;
        private Set<PhysicalObject> physicalObjects = new HashSet<>();

        public GameCharacterRunner(GameCharacter character) {
            this.character = character;
        }

        public GameCharacter getCharacter() {
            return character;
        }

        public Set<PhysicalObject> getPhysicalObjects() {
            return physicalObjects;
        }
    }

    public void addGameCharacter(GameCharacter character) {
        if (characters.containsKey(character)) {
            throw new IllegalArgumentException("Already have character " + character);
        }

        GameCharacterRunner runner = new GameCharacterRunner(character);

        character.init(new GameCharacterContext() {
            @Override
            public SpaceEngine getSpaceEngine() {
                return space;
            }

            @Override
            public void addCharacter(GameCharacter newObject) {
                GameEngine.this.addGameCharacter(newObject);
            }

            @Override
            public PhysicalObject addPhysicalObject(String name, Vector2 position, float orientation, float speedMagnitude, float speedDirection, Visualizations visualizationEngine, float collisionRadius, PhysicalObjectListener listener) {
                PhysicalObject result = space.addPhysicalObject(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius, new PhysicalObjectListener() {
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
            public void removePhysicalObject(PhysicalObject object) {
                assureMine(object);

                space.removePhysicalObject(object);
            }

            @Override
            public void removeMyself() {
                deleted.add(character);
            }

            @Override
            public List<NearPhysicalObject> findNearbyPhysicalObjects(PhysicalObject reference, float radius) {
                return space.findNearbyPhysicalObjects(reference, radius);

            }
        });

        characters.put(character, runner);
        character.start();
    }

    public void start() {
    }

    public void elapseTime(float delta) {
        space.elapseTime(delta);
        for (GameCharacter c : characters.keySet()) {
            c.elapseTime(delta);
        }

        for (GameCharacter d : deleted) {
            removeGameCharacter(d);
        }
        deleted.clear();


        if (space.getTime() > previousUpdate + updateFrequency) {
            previousUpdate = space.getTime();

            periodicUpdate();
        }
    }

    private void periodicUpdate() {
        for (Scenario scenario : scenarios) {
            scenario.periodicUpdate(space);
        }

    }
}
package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;

import java.util.*;

public class GameEngine {
    private SpaceEngine space;
    private float previousUpdate = -1f;
    private float updateFrequency = 0.1f;

    private List<Scenario> scenarios = new ArrayList<>();

    private Set<GameCharacter> characters = new HashSet<>();

    public GameEngine(SpaceEngine space) {
        this.space = space;
    }

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);

        scenario.initialUpdate(space);
    }

    public void removeGameCharacter(GameCharacter character) {
        if (characters.remove(character)) {
            character.stop();
        }
    }

    public void addGameCharacter(GameCharacter character) {
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
                // TODO: add tracking of physical objects for auto-delete

                return space.addPhysicalObject(name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius, listener);
            }

            @Override
            public void updatePhysicalObject(PhysicalObject target, String name, Vector2 position, Float orientation, Float speedMagnitude, Float speedDirection, Visualizations visualizationEngine, Float collisionRadius) {
                space.updatePhysicalObject(target, name, position, orientation, speedMagnitude, speedDirection, visualizationEngine, collisionRadius);
            }

            @Override
            public void updatePhysicalObjectExtra(PhysicalObject target, String key, Object value) {
                space.updatePhysicalObjectExtra(target, key, value);
            }

            @Override
            public void removePhysicalObject(PhysicalObject object) {
                space.removePhysicalObject(object);
            }

            @Override
            public void removeMyself() {
                removeGameCharacter(character);
            }
        });

        characters.add(character);
        character.start();
    }

    public void start() {
    }

    public void elapseTime(float delta) {
        space.elapseTime(delta);
        for (GameCharacter c : new ArrayList<>(characters)) {
            c.elapseTime(delta);
        }

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
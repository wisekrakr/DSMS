package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.*;

public class SwarmScenario extends Scenario {
    enum ScenarioState {
        INITIATION,
        LEADER,
        SWARM1,
        SWARM2
    }

    private ScenarioState state = ScenarioState.INITIATION;

    private MultifacetedNPC factionLeader;
    private Set<Protector> protectors = new HashSet<>();
    private Set<FactionWaveNPC> waveMinions = new HashSet<>();
    private Set<GameObject> targeted = new HashSet<>();
    private Set<GameObject> enemies = new HashSet<>();

    private float aggressionDistance;
    private float escapeDistance;
    private int numberOfProtectors;
    private int numberOfWaves;
    private int numberOfWaveMinions = 15;

    public SwarmScenario(float aggressionDistance, float escapeDistance, int numberOfProtectors, int numberOfWaves) {
        this.aggressionDistance = aggressionDistance;
        this.escapeDistance = escapeDistance;
        this.numberOfProtectors = numberOfProtectors;
        this.numberOfWaves = numberOfWaves;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        switch (state) {
            case INITIATION:
                initiate(spaceEngine);
                break;
            case LEADER:
                updateWithLeader(spaceEngine);
                break;
            case SWARM1:
                swarm(spaceEngine);
                break;
            case SWARM2:
                swarm(spaceEngine);
                break;

            default:
                throw new IllegalStateException("Unknown: " + state);
        }
    }

    private void initiate(SpaceEngine spaceEngine) {
        factionLeader = new MultifacetedNPC(GameHelper.randomPosition());
        spaceEngine.addGameObject(factionLeader, new SpaceEngine.GameObjectListener() {
            @Override
            public void added() {
            }

            @Override
            public void removed() {
                state = ScenarioState.SWARM1;
            }
        });
        factionLeader.cruising();
        state = ScenarioState.LEADER;
    }

    private void updateWithLeader(SpaceEngine spaceEngine) {
        Set<GameObject> newEnemies = new HashSet<>();
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target != factionLeader && !(target instanceof DebrisObject) && !(target instanceof WeaponObjectClass) && !protectors.contains(target)) {
                    if (GameHelper.distanceBetween(target.getPosition(), factionLeader.getPosition()) < aggressionDistance) {
                        if (enemies.add(target)) {
                            newEnemies.add(target);
                        }
                    }
                }
            }
        });

        Set<GameObject> escapedEnemies = new HashSet<>();
        for (GameObject enemy : enemies) {
            if (GameHelper.distanceBetween(enemy.getPosition(), factionLeader.getPosition()) > escapeDistance) {
                escapedEnemies.add(enemy);
            }
        }

        enemies.removeAll(escapedEnemies);
        targeted.removeAll(escapedEnemies);

        updateProtectors(spaceEngine);
    }

    private void swarm(SpaceEngine spaceEngine) {
        Set<GameObject> newEnemies = new HashSet<>();
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof Player) {
                    enemies.add(target);
                }
            }
        });

        wave(spaceEngine);
    }



    private void updateProtectors(SpaceEngine spaceEngine) {
        if (protectors.size() < numberOfProtectors) {
            Protector protector = new Protector(factionLeader.getPosition());

            spaceEngine.addGameObject(protector, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    protectors.add(protector);

                }

                @Override
                public void removed() {
                    protectors.remove(protector);
                }
            });

            protector.protect(factionLeader);
        }

        for (Protector protector : protectors) {
            protector.protect(factionLeader);
        }

        if (enemies.size() > 0) {
            for (GameObject enemy : enemies) {
                if (targeted.size() >= 3) {
                    break;
                }

                targeted.add(enemy);
            }

            int index = 0;
            List<GameObject> inList = new ArrayList<>(targeted);
            for (Protector protector : protectors) {
                protector.aimFor(inList.get(index));
                index = (index + 1) % inList.size();
            }
        }
    }


    private void wave(SpaceEngine spaceEngine) {
        if (!protectors.isEmpty()) {
            Set<Protector> removedProtectors = new HashSet<>();
            for (Protector protector : protectors) {
                protector.selfDestruct();
                removedProtectors.add(protector);
            }
            protectors.removeAll(removedProtectors);
        }

        if (waveMinions.size() < numberOfWaveMinions) {
            FactionWaveNPC waveMinion = new FactionWaveNPC(GameHelper.randomPosition());
            spaceEngine.addGameObject(waveMinion, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    waveMinions.add(waveMinion);
                }

                @Override
                public void removed() {
                    numberOfWaveMinions--;
                }
            });

            GameObject target = enemies.iterator().next();
            waveMinion.shootAt(target);
            //waveMinion.cruising();
        }

        if (numberOfWaveMinions == 0){
            numberOfWaveMinions = 20;
            state = ScenarioState.SWARM2;
        }

    }


}

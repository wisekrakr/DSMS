package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.*;

public class SwarmScenario extends Scenario{

    private MultifacetedNPC factionLeader;
    private Set<Protector> protectors = new HashSet<>();
    private Set<FactionWaveNPC> waveMinions = new HashSet<>();
    private Set<GameObject> targeted = new HashSet<>();
    private Set<GameObject> enemies = new HashSet<>();

    private float aggressionDistance;
    private float escapeDistance;
    private int numberOfProtectors;
    private int numberOfWaves;
    private int numberOfWaveMinions = 10;
    private boolean leaderIsAlive = false;

    public SwarmScenario(float aggressionDistance, float escapeDistance, int numberOfProtectors, int numberOfWaves) {
        this.aggressionDistance = aggressionDistance;
        this.escapeDistance = escapeDistance;
        this.numberOfProtectors = numberOfProtectors;
        this.numberOfWaves = numberOfWaves;

    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {

        if (!leaderIsAlive){
            factionLeader = new MultifacetedNPC(GameHelper.randomPosition());
            spaceEngine.addGameObject(factionLeader, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    leaderIsAlive = true;
                }

                @Override
                public void removed() {
                    factionLeader = null;
                }
            });
            factionLeader.cruising();
        }

        Set<GameObject> newEnemies = new HashSet<>();
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (factionLeader != null) {
                    if (target != factionLeader && !(target instanceof DebrisObject) && !(target instanceof WeaponObjectClass) && !protectors.contains(target)) {
                        if (GameHelper.distanceBetween(target.getPosition(), factionLeader.getPosition()) < aggressionDistance) {
                            if (enemies.add(target)) {
                                newEnemies.add(target);
                            }
                        }
                    }
                }else{
                    if (target instanceof Player){
                        enemies.add(target);
                    }
                }

            }
        });

        Set<GameObject> escapedEnemies = new HashSet<>();
        if (factionLeader != null) {
            for (GameObject enemy : enemies) {
                if (GameHelper.distanceBetween(enemy.getPosition(), factionLeader.getPosition()) > escapeDistance) {
                    escapedEnemies.add(enemy);
                }
            }
        }
        enemies.removeAll(escapedEnemies);
        targeted.removeAll(escapedEnemies);

        updateProtectors(spaceEngine);
        if (factionLeader == null) {
            wave(spaceEngine);
        }

    }

    private void updateProtectors(SpaceEngine spaceEngine) {

        if (factionLeader != null) {
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
        }else {
            Set<Protector>removedProtectors = new HashSet<>();
            for (Protector protector: protectors){
                protector.selfDestruct();
                removedProtectors.add(protector);
            }
            protectors.removeAll(removedProtectors);
        }
    }


    private void wave(SpaceEngine spaceEngine){

        if (waveMinions.size() < numberOfWaveMinions) {
            FactionWaveNPC waveMinion = new FactionWaveNPC(new Vector2(100, 100));
            spaceEngine.addGameObject(waveMinion, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    waveMinions.add(waveMinion);
                }

                @Override
                public void removed() {

                }
            });
            List<GameObject>list = new ArrayList<>(enemies);
            for (FactionWaveNPC factionWaveNPC: waveMinions) {
                //factionWaveNPC.shootAt(list.get(0));
            }
            //waveMinion.cruising();
        }
/*
        List<GameObject>list = new ArrayList<>(enemies);
        for (FactionWaveNPC factionWaveNPC: waveMinions){
            //factionWaveNPC.shootAt(list.get(0)); //TODO: Exception in thread "Thread-1" java.lang.IllegalArgumentException: Game object already present
            factionWaveNPC.cruising();
        }
        */
    }
}

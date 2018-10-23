package com.wisekrakr.firstgame.engine.scenarios;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;


import java.util.*;

public class ProtectedConvoy extends Scenario {
 /*
    private GameObject booty;
    private Set<GameObject> enemies = new HashSet<>();
    private float aggressionDistance;
    private float escapeDistance;
    private final int minMinions;
    private final int maxMinions;
    private final int targetedEnemies;
    //private Set<Protector> minions = new HashSet<>();
    private Map<GameObject, GameObject> minionTargets = new HashMap<>();

    private Set<GameObject> targeted = new HashSet<>();
    //private Iterator<Protector> iterator;

    public ProtectedConvoy(float aggressionDistance, float escapeDistance, int minMinions, int maxMinions, int targetedEnemies) {
        this.aggressionDistance = aggressionDistance;
        this.escapeDistance = escapeDistance;
        this.minMinions = minMinions;
        this.maxMinions = maxMinions;
        this.targetedEnemies = targetedEnemies;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (booty == null) {
            booty = spaceEngine.addGameObject(new Treasure( GameHelper.randomPosition()), new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {
                    booty = null;
                }
            });
        }

        Set<GameObject> newEnemies = new HashSet<>();
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target != booty && !(target instanceof DebrisObject) && !(target instanceof WeaponObjectClass) && !minions.contains(target)) {
                    if (GameHelper.distanceBetween(target.getPosition(), booty.getPosition()) < aggressionDistance) {
                        if (enemies.add(target)) {
                            newEnemies.add(target);

                            System.out.println("New: " + target);
                        }
                    }
                }
            }
        });

        Set<GameObject> escapedEnemies = new HashSet<>();
        for (GameObject enemy: enemies) {
            if (GameHelper.distanceBetween(enemy.getPosition(), booty.getPosition()) > escapeDistance) {
                escapedEnemies.add(enemy);
            }
        }
        enemies.removeAll(escapedEnemies);
        targeted.removeAll(escapedEnemies);

        if (escapedEnemies.size() > 0 || newEnemies.size() > 0) {
            System.out.println("New : " + newEnemies.size() + " escaped: " + escapedEnemies.size());
        }

        updateMinions(spaceEngine);
    }


    private void updateMinions(SpaceEngine spaceEngine) {
        if (minions.size() < Math.min(minMinions + enemies.size(), maxMinions)) {
            Protector protector = new Protector(booty.getPosition());

            spaceEngine.addGameObject(protector, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    minions.add(protector);

                }

                @Override
                public void removed() {
                    minions.remove(protector);
                }
            });

            protector.protect(booty);
        }

        int toRemove = minions.size() - minMinions - enemies.size();
        iterator = minions.iterator();
        for (int i = 0; i < toRemove; i++) {
            Protector p = iterator.next();
            iterator.remove();
            p.comeHome(booty);
        }

        for (Protector protector: minions) {
            protector.protect(booty);
        }

        if (enemies.size() > 0) {
            for (GameObject enemy: enemies) {
                if (targeted.size() >= targetedEnemies) {
                    break;
                }

                targeted.add(enemy);
            }

            int index = 0;
            List<GameObject> inList = new ArrayList<>(targeted);
            for (Protector protector: minions) {
                protector.aimFor(inList.get(index));
                index = (index + 1) % inList.size();
            }
        }
    }
    */
}

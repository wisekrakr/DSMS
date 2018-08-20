package com.wisekrakr.firstgame.engine.scenarios;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.BossMommaNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.FollowingChasingNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.Protector;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BossMommaFight extends Scenario {

    private GameObject bossMomma;
    private float aggressionDistance;
    private final int maxMinions;
    private final int targetedEnemies;
    private Set<Protector> minions = new HashSet<>();
    private Set<GameObject>targeted = new HashSet<>();
    private Set<GameObject> enemies = new HashSet<>();

    public BossMommaFight(float aggressionDistance, int maxMinions, int targetedEnemies) {
        this.aggressionDistance = aggressionDistance;
        this.maxMinions = maxMinions;
        this.targetedEnemies = targetedEnemies;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {

        if (bossMomma == null){
            bossMomma = spaceEngine.addGameObject(new BossMommaNPC(GameHelper.randomPosition()), new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {

                }
            });

        }

        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(bossMomma.getPosition(), target.getPosition()) < aggressionDistance) {
                        ((BossMommaNPC) bossMomma).aimFor(target);
                        targeted.add(target);
                    }
                }
            }
        });


        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target != bossMomma && !(target instanceof DebrisObject) && !(target instanceof WeaponObjectClass) && !minions.contains(target)) {
                    if (GameHelper.distanceBetween(target.getPosition(), bossMomma.getPosition()) < aggressionDistance) {
                        enemies.add(target);

                    }
                }
            }
        });

        updateMinions(spaceEngine);
    }

    private void updateMinions(SpaceEngine spaceEngine){
        if (bossMomma != null && minions.size() < maxMinions){
            Protector protector = new Protector(bossMomma.getPosition());

            spaceEngine.addGameObject(protector, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    minions.add(protector);
                }

                @Override
                public void removed() {
                }
            });
            protector.protect(bossMomma);
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
            if (minions.size() != 0) {
                for (Protector protector : minions) {

                    GameObject target = inList.get(index);
                    protector.aimFor(target);
                    index = (index + 1) % inList.size();

                    if (bossMomma == null || bossMomma.getHealth() <= 0) {
                        protector.selfDestruct();
                    }
                }
            }
        }
    }
}

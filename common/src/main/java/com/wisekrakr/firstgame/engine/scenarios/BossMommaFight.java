package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
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

    private int initialNumberOfMinions;

    enum ScenarioState {
        INITIATION,
        BOSS_MOMMA,
        STAGE1,
        STAGE2,
        BOSS_IS_DEAD
    }

    private ScenarioState state = ScenarioState.INITIATION;

    private BossMommaNPC bossMomma;
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
        initialNumberOfMinions = maxMinions;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {

        switch (state){
            case INITIATION:
                initiate(spaceEngine);
                break;
            case BOSS_MOMMA:
                updateBossMomma(spaceEngine);
                break;
            case STAGE1:
                updateMinions(spaceEngine);
                break;
            case STAGE2:
                readyStage2(spaceEngine);
                break;
            case BOSS_IS_DEAD:
                missionEnd();
                break;
            default:
                throw new IllegalStateException("Unknown: " + state);
        }
    }

    private void initiate(SpaceEngine spaceEngine){
        bossMomma = new BossMommaNPC(GameHelper.randomPosition());
        spaceEngine.addGameObject(bossMomma, new SpaceEngine.GameObjectListener() {
            @Override
            public void added() {

            }

            @Override
            public void removed() {

            }
        });
        bossMomma.announcePresence();
        state = ScenarioState.BOSS_MOMMA;

    }

    private void updateBossMomma(SpaceEngine spaceEngine){
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(bossMomma.getPosition(), target.getPosition()) < aggressionDistance && initialNumberOfMinions == maxMinions) {
                        bossMomma.primaryAttack(target);
                        targeted.add(target);
                        state = ScenarioState.STAGE1;
                        System.out.println("Primary Attack on target: " + target);
                    }
                }
            }
        });
    }

    private void updateMinions(SpaceEngine spaceEngine){

        if (initialNumberOfMinions != 0) {
            if (bossMomma != null && minions.size() < maxMinions) {
                Protector protector = new Protector(new Vector2(bossMomma.getPosition().x + bossMomma.getCollisionRadius(),
                        bossMomma.getPosition().y + bossMomma.getCollisionRadius()));

                spaceEngine.addGameObject(protector, new SpaceEngine.GameObjectListener() {
                    @Override
                    public void added() {
                        minions.add(protector);
                    }

                    @Override
                    public void removed() {
                        minions.remove(protector);
                        initialNumberOfMinions--;
                    }
                });
                protector.protect(bossMomma);
            }

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

            if (enemies.size() > 0) {
                for (GameObject enemy : enemies) {
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
        }else {
            state = ScenarioState.STAGE2;
        }
    }

    private void readyStage2(SpaceEngine spaceEngine){
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(bossMomma.getPosition(), target.getPosition()) < aggressionDistance) {
                        bossMomma.secondaryAttack(target);
                        System.out.println("Secondary Attack on target: " + target);
                        if (bossMomma == null){
                            state = ScenarioState.BOSS_IS_DEAD;
                            System.out.println("Ding Dong the Boss is Dead");
                        }
                    }
                }
            }
        });
    }

    private void missionEnd(){
        System.out.println("Mission End");
    }
}

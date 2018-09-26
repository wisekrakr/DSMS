package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.KillMission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.MultifacetedNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PackageObject;
import com.wisekrakr.firstgame.server.ScenarioHelper;

import java.util.*;

public class TravellerWithMission extends Scenario {

    private static GameObjectFactory<?>[] POSSIBLE_ENEMIES = {
            ScenarioHelper.CRAZY_SPAWNER_FACTORY,
            ScenarioHelper.CHASING_SHOOTING_FACTORY,
            ScenarioHelper.MISSILE_SHOOTER_FACTORY
    };

    enum ScenarioState {
        INITIATION,
        MISSION_SELECT,
        MISSION_DROP,
        PACKAGE_DROP,
        MISSION_STAGE_KILL,
        MISSION_STAGE_PACKAGE,
        MISSION_STAGE_DELIVERY,
        MISSION_END
    }

    private ScenarioState state = ScenarioState.INITIATION;

    private Set<ScenarioState> scenarioStates = new HashSet<>();

    private final int initialNumTargets;
    private float spawnInterval;
    private MultifacetedNPC traveller;
    private PackageObject packageObject;
    private MultifacetedNPC packageReceiver;
    private Set<Mission> missions = new HashSet<>();
    private Set<PackageObject>packageObjects = new HashSet<>();
    private Set<GameObject> targets = new HashSet<>();
    private int numTargets;
    private float chaseDistance;
    private int numOfMissions = 1;
    private int numOfPackages = 1;
    private GameObjectFactory<?> targetFactory;
    private float lastTravellerSpawned;

    //TODO: Works, but needs some cleaner code. If mission is package and when done, new missions are 2 instead of 1 etc.

    public TravellerWithMission(float chaseDistance, int numTargets, float spawnInterval) {
        this.chaseDistance = chaseDistance;
        this.numTargets = numTargets;
        initialNumTargets = numTargets;
        this.spawnInterval = spawnInterval;

    }

    @Override
    public void initialUpdate(SpaceEngine spaceEngine) {
        int target = GameHelper.randomGenerator.nextInt(POSSIBLE_ENEMIES.length);

        targetFactory = POSSIBLE_ENEMIES[target];
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        switch (state) {
            case INITIATION:
                initiate(spaceEngine);
                break;
            case MISSION_SELECT:
                updateTraveller(spaceEngine);
                break;
            case MISSION_DROP:
                updateMission(spaceEngine);
                break;
            case MISSION_STAGE_KILL:
                dropTargets(spaceEngine);
                break;
            case PACKAGE_DROP:
                packageDrop(spaceEngine);
                break;
            case MISSION_STAGE_PACKAGE:
                postman(spaceEngine);
                break;
            case MISSION_STAGE_DELIVERY:
                delivery(spaceEngine);
                break;
            case MISSION_END:
                missionEnd();
                break;
            default:
                throw new IllegalStateException("Unknown: " + state);
        }
    }

    private void initiate(SpaceEngine spaceEngine){
        if (lastTravellerSpawned == 0){
            lastTravellerSpawned = spaceEngine.getTime();
        }

        scenarioStates.add(ScenarioState.MISSION_DROP);
        scenarioStates.add(ScenarioState.PACKAGE_DROP);

        if (spaceEngine.getTime() - lastTravellerSpawned > spawnInterval) {
            traveller = new MultifacetedNPC(GameHelper.randomPosition());
            spaceEngine.addGameObject(traveller, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    state = ScenarioState.MISSION_SELECT;
                    System.out.println("Traveller spawned: " + traveller.getPosition());
                }

                @Override
                public void removed() {

                }
            });
            traveller.cruising();
        }
    }

    private void updateTraveller(SpaceEngine spaceEngine){
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                /*
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(traveller, target) < chaseDistance) {
                        traveller.chasing(target);
                        if (GameHelper.distanceBetween(traveller, target) < chaseDistance / 2) {
                            traveller.fullStop();
                            state = (ScenarioState) scenarioStates.toArray()[GameHelper.randomGenerator.nextInt(scenarioStates.size())];
                            System.out.println("Your mission: " + state.name());
                        }
                    }
                }
                */
            }
        });
    }

    private void updateMission(SpaceEngine spaceEngine) {

        if (missions.size() < numOfMissions && traveller != null) {
            float x = traveller.getPosition().x;
            float y = traveller.getPosition().y;

            float deltaX = (float) Math.cos(traveller.getOrientation());
            float deltaY = (float) Math.sin(traveller.getOrientation());

            String name = targetFactory.create(new Vector2(0, 0), 0f, 0f).getName();

            KillMission mission = new KillMission(new Vector2(x + traveller.getCollisionRadius() * deltaX,
                    y + traveller.getCollisionRadius() * deltaY), name);

            spaceEngine.addGameObject(mission, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    missions.add(mission);
                    System.out.println("MISSION ADDED: " + mission.className() + " ,Target: " + name);
                }

                @Override
                public void removed() {
                    missions.remove(mission);
                    traveller.cruising();
                    numOfMissions--;
                    if (mission.isPickedUp()) {
                        traveller.fullStop();
                        state = ScenarioState.MISSION_STAGE_KILL;
                    }
                }
            });
        }
    }

    private void dropTargets(SpaceEngine spaceEngine) {

        if (initialNumTargets != 0) {
            if (targets.size() < numTargets) {

                GameObject npc = targetFactory.create(GameHelper.randomPosition(), GameHelper.randomDirection(),
                        GameHelper.generateRandomNumberBetween(200f, 400f));
                spaceEngine.addGameObject(npc, new SpaceEngine.GameObjectListener() {
                    @Override
                    public void added() {
                        targets.add(npc);
                    }

                    @Override
                    public void removed() {
                        targets.remove(npc);
                        numTargets--;

                    }
                });
            }else if (targets.size() == 0) {
                state = ScenarioState.MISSION_END;
            }
            System.out.println("Number of missions= " + numOfMissions + " ,Number of Targets= " + targets.size());
        }
    }

    private void packageDrop(SpaceEngine spaceEngine){

        if (packageObjects.size() < numOfPackages && traveller != null){

            float x = traveller.getPosition().x;
            float y = traveller.getPosition().y;

            float deltaX = (float) Math.cos(traveller.getOrientation());
            float deltaY = (float) Math.sin(traveller.getOrientation());

            packageObject = new PackageObject(new Vector2(x + traveller.getCollisionRadius() * deltaX,
                    y + traveller.getCollisionRadius() * deltaY), traveller);
            spaceEngine.addGameObject(packageObject, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    System.out.println("Package Dropped: " + traveller.getName() + " --  Get there before they die of waiting!");
                    packageObjects.add(packageObject);
                    numOfPackages--;
                }

                @Override
                public void removed() {
                    packageObjects.remove(packageObject);
                }
            });
            packageObject.sendOrder();

            state = ScenarioState.MISSION_STAGE_PACKAGE;

        }

        if (!packageObjects.isEmpty()){
            packageReceiver = new MultifacetedNPC(GameHelper.randomPosition());
            spaceEngine.addGameObject(packageReceiver, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {
                    packageObject.missionEnd();
                    System.out.println("Package receiver died");
                }
            });
        }
    }

    private void postman(SpaceEngine spaceEngine){
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                /*
                if (target instanceof Player){
                    if (GameHelper.distanceBetween(packageObject, target) < 80f){
                        packageObject.inPostmanCare(target);
                        state = ScenarioState.MISSION_STAGE_DELIVERY;
                        System.out.println("Package with player, send to: " + packageReceiver.getName());
                    }
                }
                */
            }
        });
    }

    private void delivery(SpaceEngine spaceEngine){
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof PackageObject){
                    if (GameHelper.distanceBetween(packageReceiver, packageObject)< 80f){
                        packageObject.delivery(packageReceiver);
                        System.out.println("Package delivered: " + packageObject.getName());
                        missionEnd();
                        packageObjects.remove(packageObject);
                        packageReceiver.missionComplete();

                    }
                }
            }
        });


    }

    private void missionEnd(){
        traveller.missionComplete();
        numTargets = initialNumTargets;
        numOfMissions++;
        numOfPackages++;
        lastTravellerSpawned = 0;

        System.out.println("Mission Complete! Number of new Missions: " + numOfMissions);

        state = ScenarioState.INITIATION;
    }

    private void missionFailed(){
        numTargets = initialNumTargets;
        numOfMissions++;
        numOfPackages++;

        lastTravellerSpawned = 0;

        System.out.println("Mission Failed! Number of new Missions: " + numOfMissions);

        state = ScenarioState.INITIATION;
    }
}

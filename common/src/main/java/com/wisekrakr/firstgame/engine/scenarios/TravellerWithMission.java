package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.MultifacetedNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PackageObject;
import com.wisekrakr.firstgame.server.ScenarioHelper;

import java.util.HashSet;
import java.util.Set;

public class TravellerWithMission extends Scenario {

    public static GameObjectFactory<?>[] POSSIBLE_ENEMIES = {
            ScenarioHelper.CRAZY_SPAWNER_FACTORY,
            ScenarioHelper.CHASING_SHOOTING_FACTORY,
            ScenarioHelper.MISSILE_SHOOTER_FACTORY
    };

    enum ScenarioState {
        INITIATION,
        TRAVELLER,
        MISSION_START,
        MISSION_STAGE_KILL,
        PACKAGE_GIVER,
        PACKAGE_GIVER_BEHAVIOR,
        MISSION_STAGE_DELIVERY,
        MISSION_END
    }

    private ScenarioState state = ScenarioState.INITIATION;

    private final int initialNumTargets;
    private float spawnInterval;
    private MultifacetedNPC traveller;
    private MultifacetedNPC packageGiver;
    private Set<Mission> missions = new HashSet<>();
    private Set<GameObject> targets = new HashSet<>();
    private int numTargets;
    private float chaseDistance;
    private int numOfMissions = 1;
    private int numOfPackages = 1;
    private GameObjectFactory<?> targetFactory;
    private float lastTravellerSpawned;

    //TODO: set of packages? link between package and traveller (and Player==> another if(boolean clingOn){search for traveller} )?
    //TODO: traveller must be open to receive package. Need a behavior to do that

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
            case TRAVELLER:
                updateTraveller(spaceEngine);
                break;
            case MISSION_START:
                updateMission(spaceEngine);
                break;
            case MISSION_STAGE_KILL:
                dropTargets(spaceEngine);
                break;
            case PACKAGE_GIVER:
                dropPackageGiver(spaceEngine);
                break;
            case PACKAGE_GIVER_BEHAVIOR:
                packageGiverBehavior(spaceEngine);
                break;
            case MISSION_STAGE_DELIVERY:
                packageDrop(spaceEngine);
                break;
            case MISSION_END:
                missionEnd();
                break;
            default:
                throw new IllegalStateException("Unknown: " + state);
        }
        System.out.println(state);

    }

    private void initiate(SpaceEngine spaceEngine){
        if (lastTravellerSpawned == 0){
            lastTravellerSpawned = spaceEngine.getTime();
        }

        if (spaceEngine.getTime() - lastTravellerSpawned > spawnInterval) {
            traveller = new MultifacetedNPC(GameHelper.randomPosition());
            spaceEngine.addGameObject(traveller, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    state = ScenarioState.TRAVELLER;
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
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(traveller, target) < chaseDistance) {
                        traveller.chasing(target);
                        if (GameHelper.distanceBetween(traveller, target) < chaseDistance / 2) {
                            traveller.fullStop();
                            state = ScenarioState.MISSION_START;
                        }
                    }
                }
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

            Mission mission = new Mission(name ,new Vector2(x + traveller.getCollisionRadius() * deltaX,
                    y + traveller.getCollisionRadius() * deltaY));

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
                        state = ScenarioState.PACKAGE_GIVER;
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

    private void dropPackageGiver(SpaceEngine spaceEngine){

        packageGiver = new MultifacetedNPC(GameHelper.randomPosition());

        spaceEngine.addGameObject(packageGiver, new SpaceEngine.GameObjectListener() {
            @Override
            public void added() {
                state = ScenarioState.PACKAGE_GIVER_BEHAVIOR;
            }

            @Override
            public void removed() {

            }
        });
    }

    private void packageGiverBehavior(SpaceEngine spaceEngine){
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof Player){
                    if (GameHelper.distanceBetween(packageGiver, target) < chaseDistance/2){
                        state = ScenarioState.MISSION_STAGE_DELIVERY;
                    }else {
                        packageGiver.cruising();
                    }
                }
            }
        });
    }

    private void packageDrop(SpaceEngine spaceEngine){

        if (numOfPackages == 1){

            float x = packageGiver.getPosition().x;
            float y = packageGiver.getPosition().y;

            float deltaX = (float) Math.cos(packageGiver.getOrientation());
            float deltaY = (float) Math.sin(packageGiver.getOrientation());

            PackageObject packageObject = new PackageObject(new Vector2(x + packageGiver.getCollisionRadius() * deltaX,
                    y + packageGiver.getCollisionRadius() * deltaY), packageGiver);
            spaceEngine.addGameObject(packageObject, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    System.out.println("Package Dropped: " + packageObject.getName());
                    numOfPackages--;
                }

                @Override
                public void removed() {
                    state = ScenarioState.MISSION_END;
                }
            });
            if (GameHelper.distanceBetween(packageObject.getPosition(), traveller.getPosition()) < 100){
                spaceEngine.removeGameObject(packageObject);
            }
            System.out.println(GameHelper.distanceBetween(packageObject.getPosition(), traveller.getPosition()));
        }

    }

    private void missionEnd(){
        traveller.missionComplete();
        numTargets = initialNumTargets;
        numOfMissions++;
        lastTravellerSpawned = 0;

        System.out.println("Mission Complete! Number of new Missions: " + numOfMissions);

        state = ScenarioState.INITIATION;
    }
}

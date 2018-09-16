package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.FrigateNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.PackageObject;

import java.util.*;

public class RetrieveThePackagesInTime extends Scenario {

    private int packagesGot;

    enum ScenarioState {
        INITIATION,
        FRIGATE,
        DROPPING_LOAD,
        START_TIMER,
        MISSION_END
    }

    private ScenarioState state = ScenarioState.INITIATION;

    private FrigateNPC shipWithLotsOfCrapOnIt;
    private Set<PackageObject> packages = new HashSet<>();
    private Set<PackageObject> toBeRemovedPackages = new HashSet<>();
    private float escapeDistance;
    private int numOfPackages;
    private int initialNumOfPackages;
    private float timeToRetrieve;
    private float timer;

    public RetrieveThePackagesInTime(float escapeDistance, int numOfPackages, float timeToRetrieve) {
        this.escapeDistance = escapeDistance;
        this.numOfPackages = numOfPackages;
        this.timeToRetrieve = timeToRetrieve;
        initialNumOfPackages = numOfPackages;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {

        switch (state){
            case INITIATION:
                initiate(spaceEngine);
                break;
            case FRIGATE:
                frigate(spaceEngine);
                break;
            case DROPPING_LOAD:
                dropPackages(spaceEngine);
                break;
            case START_TIMER:
                timer(spaceEngine);
                break;
            case MISSION_END:
                missionEnd();
                break;
            default:
                throw new IllegalStateException("Unknown: " + state);
        }
    }

    private void initiate(SpaceEngine spaceEngine){

        shipWithLotsOfCrapOnIt = new FrigateNPC(GameHelper.randomPosition());
        spaceEngine.addGameObject(shipWithLotsOfCrapOnIt, new SpaceEngine.GameObjectListener() {
            @Override
            public void added() {
                System.out.println("Frigate spawned: " + shipWithLotsOfCrapOnIt.getPosition());
            }

            @Override
            public void removed() {

            }
        });
        shipWithLotsOfCrapOnIt.cruising();
        state = ScenarioState.FRIGATE;

    }

    private void frigate(SpaceEngine spaceEngine){
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(target.getPosition(), shipWithLotsOfCrapOnIt.getPosition()) < escapeDistance) {
                        shipWithLotsOfCrapOnIt.escaping(target);
                        state = ScenarioState.DROPPING_LOAD;
                        System.out.println("Frigate is running from: " + target);
                    }
                }
            }
        });
    }

    private void dropPackages(SpaceEngine spaceEngine) {
        shipWithLotsOfCrapOnIt.cruising();
        if (packages.size() < numOfPackages && shipWithLotsOfCrapOnIt != null) {
            float x = shipWithLotsOfCrapOnIt.getPosition().x;
            float y = shipWithLotsOfCrapOnIt.getPosition().y;

            PackageObject packageObject = new PackageObject(new Vector2(x + shipWithLotsOfCrapOnIt.getCollisionRadius(),
                    y + shipWithLotsOfCrapOnIt.getCollisionRadius()), shipWithLotsOfCrapOnIt);

            spaceEngine.addGameObject(packageObject, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    packages.add(packageObject);
                    System.out.println("Dropped load of: " + packages.size() + " package(s)");
                }

                @Override
                public void removed() {
                    packages.remove(packageObject);
                    numOfPackages--;
                    System.out.println("Package removed: " + packageObject.getName());
                }
            });
            packageObject.sendOrder();
            if (packages.size() == initialNumOfPackages) {
                state = ScenarioState.START_TIMER;
            }
        }
    }

    private void timer(SpaceEngine spaceEngine){

        int clock = (int) (spaceEngine.getTime() % 100);

        if (clock < timeToRetrieve - spaceEngine.getTime()) {

            for (PackageObject p : packages) {
                spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
                    @Override
                    public void doIt(GameObject target) {
                        if (GameHelper.distanceBetween(p, target) < 100) {
                            if (target instanceof Player) {
                                p.timedMissionInProgress(target);
                                toBeRemovedPackages.add(p);
                            }
                        }
                    }
                });
            }

            if (toBeRemovedPackages.size() == initialNumOfPackages) {
                state = ScenarioState.MISSION_END;
                System.out.println("Mission Successful: " + toBeRemovedPackages.size() + " packages picked up!");
            }

        }else if (clock == timeToRetrieve && packages.size() > 0) {
            System.out.println("Mission Failed: " + (packages.size() - toBeRemovedPackages.size()) + " packages still floating around!");
            state = ScenarioState.MISSION_END;
        }

        for (PackageObject p: toBeRemovedPackages){
            packages.remove(p);
        }

        System.out.println("Time left: " + (timeToRetrieve - clock) + " ,Clock: " + (timeToRetrieve - spaceEngine.getTime())  +
                " ,Packages left: " + packages.size() + " ,Packages got: " + toBeRemovedPackages.size());

    }

    private void missionEnd(){
        numOfPackages = initialNumOfPackages;
        shipWithLotsOfCrapOnIt.missionComplete();
    }

}

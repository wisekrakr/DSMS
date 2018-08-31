package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.*;

import java.util.*;

public class DamselInDistress extends Scenario {

    private Damsel damsel;
    private Set<Pervert> perverts = new HashSet<>();
    private float runToDistance;
    private float escapeDistance;
    private final int maxPerverts;
    private Set<MissionEnding> missionEndings = new HashSet<>();
    private int numOfEndings = 1;

    public DamselInDistress(float runToDistance, float escapeDistance, int maxPerverts) {
        this.runToDistance = runToDistance;
        this.escapeDistance = escapeDistance;
        this.maxPerverts = maxPerverts;

    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (damsel == null) {
            damsel = new Damsel( GameHelper.randomPosition());
            spaceEngine.addGameObject(damsel, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {
                    damsel = null;

                }

            });
            damsel.lookingForAHero();
        }
        updateDamsel(spaceEngine);
        if (damsel.isClingingOn()) {
            bringDamselToSafeLocation(spaceEngine);
        }

    }

    private void updateDamsel(SpaceEngine spaceEngine){

        if (perverts.size() < maxPerverts){
            Pervert pervert = new Pervert(GameHelper.randomPosition());

            spaceEngine.addGameObject(pervert, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    perverts.add(pervert);
                }

                @Override
                public void removed() {
                    perverts.remove(pervert);
                }
            });
            pervert.lookingForADamsel();
        }

        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                for (Pervert pervert: perverts){
                    if (target == damsel) {
                        if (GameHelper.distanceBetween(pervert.getPosition(), target.getPosition()) < escapeDistance) {
                            pervert.chaseAfter(target);
                            if (!damsel.isClingingOn()) {
                                damsel.runFrom(pervert);
                            }
                        }
                        if (GameHelper.distanceBetween(pervert.getPosition(), target.getPosition()) < escapeDistance/3) {
                            pervert.cirkelingDamsel(target);
                        }
                    }
                }
                if (target instanceof Player){
                    if (GameHelper.distanceBetween(damsel.getPosition(), target.getPosition()) < runToDistance) {
                        damsel.clingOn(target);
                    }
                }
            }
        });
    }

    private void bringDamselToSafeLocation(SpaceEngine spaceEngine){

        if (missionEndings.size() < numOfEndings){
            System.out.println("Adding a mission end");

            MissionEnding missionEnd = new MissionEnding(GameHelper.randomPosition());

            spaceEngine.addGameObject(missionEnd, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    missionEndings.add(missionEnd);
                }

                @Override
                public void removed() {
                    System.out.println("Mission end removed");
                    damsel.missionComplete();
                    numOfEndings++;

                }
            });
        }
    }

    private class MissionEnding extends Mission{

        private MissionEnding(Vector2 initialPosition) {
            super(GameObjectVisualizationType.MISSION_END, "End of Mission", initialPosition);
            setCollisionRadius(40f);
        }

        @Override
        public Map<String, Object> getExtraSnapshotProperties() {
            Map<String, Object> result = new HashMap<String, Object>();

            result.put("radius", getCollisionRadius());

            return result;
        }

        @Override
        public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
            System.out.println("Collide with " + subject);
            if (subject == damsel){
                System.out.println("  its the damsel");
                toDelete.add(this);
            }

        }

        @Override
        public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        }
    }
}

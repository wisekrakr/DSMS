package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.*;

public class DamselInDistress extends Scenario {
    private GameObject missionEnd;
    private GameObject damsel;
    private boolean damselCanBeRescued = false;
    private Set<GameObject> enemies = new HashSet<>();
    private float runToDistance;
    private float escapeDistance;
    private final int maxMinions;

    public DamselInDistress(float runToDistance, float escapeDistance, int maxMinions) {
        this.runToDistance = runToDistance;
        this.escapeDistance = escapeDistance;
        this.maxMinions = maxMinions;

    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (damsel == null) {
            damsel = spaceEngine.addGameObject(new Damsel( GameHelper.randomPosition()), new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {
                    //damsel = null;
                }
            });
        }


        updateDamsel(spaceEngine);
        if (missionEnd == null) {
            bringDamselToSafeLocation(spaceEngine);
        }
    }

    private void updateDamsel(SpaceEngine spaceEngine){

        if (enemies.size() <  maxMinions){
            Pervert pervert = new Pervert(GameHelper.randomPosition());

            spaceEngine.addGameObject(pervert, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    enemies.add(pervert);
                }

                @Override
                public void removed() {

                }
            });
        }

        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                for (GameObject object: enemies){
                    if (target != object && !(target instanceof DebrisObject) && !(target instanceof WeaponObjectClass) && !enemies.contains(target)) {
                        if (GameHelper.distanceBetween(object.getPosition(), target.getPosition()) < escapeDistance) {
                            if (target instanceof Damsel){
                                damsel = target;
                                if (!((Damsel) damsel).isClingingOn()) {
                                    ((Damsel) damsel).runFrom(object);
                                }
                            }else {
                                ((Damsel) damsel).lookingForAHero();
                            }
                        }
                    }
                }
                if (target instanceof Player && damsel instanceof Damsel){
                    if (GameHelper.distanceBetween(damsel.getPosition(), target.getPosition()) < runToDistance) {
                        ((Damsel) damsel).clingOn(target);
                        damselCanBeRescued = true;
                    }else {
                        ((Damsel) damsel).lookingForAHero();
                    }
                }
            }
        });
    }

    private void bringDamselToSafeLocation(SpaceEngine spaceEngine){
        if (damselCanBeRescued){
            if (missionEnd == null){
                missionEnd = spaceEngine.addGameObject(new MissionEnding(GameHelper.randomPosition()), new SpaceEngine.GameObjectListener() {
                    @Override
                    public void added() {

                    }

                    @Override
                    public void removed() {
                        if (((Damsel) damsel).isClingingOn()) {
                            ((Damsel) damsel).missionComplete();
                        }
                    }
                });

            }
        }
    }

    private class MissionEnding extends Mission{

        public MissionEnding(Vector2 initialPosition) {
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
            if (subject instanceof Damsel){
                toDelete.add(this);
            }
        }
    }



}

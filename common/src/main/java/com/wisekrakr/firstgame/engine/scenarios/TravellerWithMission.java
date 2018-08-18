package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.KillMission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.NonPlayerCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.CrazilySpawningPassiveAggressiveNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.DebrisObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.WearyTravellerFriendlyNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects.WeaponObjectClass;

import java.util.HashSet;
import java.util.Set;

public class TravellerWithMission extends Scenario {

    private GameObject traveller;
    private Set<Mission> missions = new HashSet<>();
    private Set<GameObject> enemies = new HashSet<>();
    private float chaseDistance;
    private GameObject target;

    public TravellerWithMission(float chaseDistance) {
        this.chaseDistance = chaseDistance;
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {

        if (traveller == null){
            traveller = spaceEngine.addGameObject(new WearyTravellerFriendlyNPC(GameHelper.randomPosition()), new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {
                    traveller = null;
                }
            });

        }

        updateMission(spaceEngine);
    }


    private void updateMission(SpaceEngine spaceEngine){

        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (traveller instanceof WearyTravellerFriendlyNPC) {
                    if (target instanceof Player) {
                        if (GameHelper.distanceBetween(traveller, target) < chaseDistance / 2) {
                            dropMission(spaceEngine);
                            System.out.println("DROPPED MISSION   " + enemies.size());
                            if (enemies.size() == 0){
                                ((WearyTravellerFriendlyNPC) traveller).missionIsComplete();
                            }
                        }else {
                            ((WearyTravellerFriendlyNPC) traveller).chaseToGiveMission(target);
                        }
                    }
                }
            }
        });
    }

    private void dropMission(SpaceEngine spaceEngine){

        if (enemies.size() < 4){
            NonPlayerCharacter npc = new CrazilySpawningPassiveAggressiveNPC(GameHelper.randomPosition(), GameHelper.generateRandomNumberBetween(200f, 400f));

            spaceEngine.addGameObject(npc, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    enemies.add(npc);
                }

                @Override
                public void removed() {

                }
            });
            target = npc;
        }

        if (missions.size() < 1 && traveller != null){

            float x = traveller.getPosition().x;
            float y = traveller.getPosition().y;

            float deltaX = (float) Math.cos(traveller.getOrientation());
            float deltaY = (float) Math.sin(traveller.getOrientation());

            Mission mission = new KillMission(new Vector2(x + traveller.getCollisionRadius() * deltaX,
                    y + traveller.getCollisionRadius() * deltaY));

            spaceEngine.addGameObject(mission, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    missions.add(mission);
                }

                @Override
                public void removed() {
                    
                }
            });
        }
    }


}

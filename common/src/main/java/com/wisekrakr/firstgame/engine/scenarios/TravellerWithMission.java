package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.missions.Mission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.KillMission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.AsteroidWatchingMissileShootingNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.CrazilySpawningPassiveAggressiveNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.FollowingChasingNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.MultifacetedNPC;
import com.wisekrakr.firstgame.server.ScenarioHelper;

import java.util.HashSet;
import java.util.Set;

public class TravellerWithMission extends Scenario {
    private static GameObjectFactory<?>[] POSSIBLE_ENEMIES = {
            ScenarioHelper.CRAZY_SPAWNER_FACTORY,
            ScenarioHelper.CHASING_SHOOTING_FACTORY,
            ScenarioHelper.MISSILE_SHOOTER_FACTORY
    };

    private final int initialNumTargets;
    private MultifacetedNPC traveller;
    private Set<Mission> missions = new HashSet<>();
    private Set<GameObject> targets = new HashSet<>();
    private int numTargets;
    private float chaseDistance;
    private boolean missionDropped;
    private int numOfMissions = 1;
    private int p = 0;
    private GameObjectFactory<?> targetFactory;

    public TravellerWithMission(float chaseDistance, int numTargets) {
        this.chaseDistance = chaseDistance;
        this.numTargets = numTargets;
        initialNumTargets = numTargets;
    }

    @Override
    public void initialUpdate(SpaceEngine spaceEngine) {
        int target = GameHelper.randomGenerator.nextInt(POSSIBLE_ENEMIES.length);

        targetFactory = POSSIBLE_ENEMIES[target];
    }

    @Override
    public void periodicUpdate(SpaceEngine spaceEngine) {

        if (traveller == null) {
            traveller = new MultifacetedNPC(GameHelper.randomPosition());
            spaceEngine.addGameObject(traveller, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {

                }

                @Override
                public void removed() {
                    traveller = null;
                }
            });
            traveller.cruising();
        }

        updateMission(spaceEngine);

    }


    private void updateMission(SpaceEngine spaceEngine) {
        spaceEngine.forAllObjects(new SpaceEngine.GameObjectHandler() {
            @Override
            public void doIt(GameObject target) {
                if (target instanceof Player) {
                    if (GameHelper.distanceBetween(traveller, target) < chaseDistance) {
                        traveller.chasing(target);
                        if (GameHelper.distanceBetween(traveller, target) < chaseDistance / 2) {
                            missionDropped = true;
                            traveller.fullStop();
                        } else if (missionDropped) {
                            traveller.cruising();
                        }
                    }
                }
            }
        });

        if (missions.size() < numOfMissions && traveller != null && missionDropped) {
            float x = traveller.getPosition().x;
            float y = traveller.getPosition().y;

            float deltaX = (float) Math.cos(traveller.getOrientation());
            float deltaY = (float) Math.sin(traveller.getOrientation());

            String name = targetFactory.create(new Vector2(0, 0), 0f, 0f).getName();

            Mission mission = new KillMission(new Vector2(x + traveller.getCollisionRadius() * deltaX,
                    y + traveller.getCollisionRadius() * deltaY), name);

            spaceEngine.addGameObject(mission, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    missions.add(mission);
                    System.out.println("MISSION NAME  " + name);
                }

                @Override
                public void removed() {
                    missions.remove(mission);
                    numOfMissions--;
                }
            });
        }

        if (missionDropped && missions.size() == 0) {
            dropTargets(spaceEngine);
            System.out.println("Number of missions= " + numOfMissions + " ,Number of Enemies= " + targets.size());
            if (targets.size() == 0) {
                traveller.missionComplete();
                missionDropped = false;
                numTargets = initialNumTargets;
                numOfMissions++;
            }
        }
    }

    private void dropTargets(SpaceEngine spaceEngine) {
        if (targets.size() < numTargets) {
            GameObject npc = targetFactory.create(GameHelper.randomPosition(), 0, GameHelper.generateRandomNumberBetween(200f, 400f));

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
        }
    }
/*
    private GameObject chosenTarget(){
        int randomNPC = MathUtils.random(1,3);

        GameObject n = null;
        switch (randomNPC) {
            case 1:
                n = new CrazilySpawningPassiveAggressiveNPC(GameHelper.randomPosition(), GameHelper.generateRandomNumberBetween(200f, 400f));
                break;
            case 2:
                n = new FollowingChasingNPC(GameHelper.randomPosition(), GameHelper.generateRandomNumberBetween(200f, 400f));
                break;
            case 3:
                n = new AsteroidWatchingMissileShootingNPC(GameHelper.randomPosition(), GameHelper.generateRandomNumberBetween(200f, 400f));
                break;
        }
        return n;
    }
    */
}
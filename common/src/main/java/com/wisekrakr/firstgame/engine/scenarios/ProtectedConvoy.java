package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.Tags;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectEvictionPolicy;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;


import java.util.*;

public class ProtectedConvoy extends Scenario {

    enum ScenarioState{
        INITIATION, MINIONS_UPDATE, MINIONS_CREATION
    }
    private ScenarioState state = ScenarioState.INITIATION;

    private Set<GameCharacter>minions = new HashSet<>();

    private float aggressionDistance;
    private float escapeDistance;
    private final int minMinions;
    private final int maxMinions;
    private final int targetedEnemies;


    public ProtectedConvoy(float aggressionDistance, float escapeDistance, int minMinions, int maxMinions, int targetedEnemies) {
        this.aggressionDistance = aggressionDistance;
        this.escapeDistance = escapeDistance;
        this.minMinions = minMinions;
        this.maxMinions = maxMinions;
        this.targetedEnemies = targetedEnemies;
    }

    @Override
    public void periodicUpdate() {

        switch (state){
            case INITIATION:
                initiate();
                break;
            case MINIONS_CREATION:

                break;
            case MINIONS_UPDATE:

                break;
        }
    }

    private void initiate(){
        getContext().space().addPhysicalObject("treasure",
                getContext().space().chooseCreationPoint(),
                GameHelper.randomDirection(),
                0,
                0,
                Visualizations.TREASURE,
                20f,
                PhysicalObjectEvictionPolicy.SLOW,
                new PhysicalObjectListener() {
                    @Override
                    public void collision(PhysicalObject myself, PhysicalObject two, float time, Vector2 epicentre, float impact) {
                        if (two.getTags().contains(Tags.PLAYER)){
                            getContext().removeMyself();

                            //player gets something.
                        }
                    }

                    @Override
                    public void removed(PhysicalObject target) {
                        getContext().removeMyself();
                    }
                }
        );
    }

//    private void initiateMinions(){
//
//        Set<GameCharacter> newEnemies = new HashSet<>();
//
//        getContext().space().
//
//    }
//
//    private void updateMinions() {
//        if (minions.size() < Math.min(minMinions + enemies.size(), maxMinions)) {
//            Protector protector = new Protector(booty.getPosition());
//
//            spaceEngine.addGameObject(protector, new SpaceEngine.GameObjectListener() {
//                @Override
//                public void added() {
//                    minions.add(protector);
//
//                }
//
//                @Override
//                public void removed() {
//                    minions.remove(protector);
//                }
//            });
//
//            protector.protect(booty);
//        }
//
//        int toRemove = minions.size() - minMinions - enemies.size();
//        iterator = minions.iterator();
//        for (int i = 0; i < toRemove; i++) {
//            Protector p = iterator.next();
//            iterator.remove();
//            p.comeHome(booty);
//        }
//
//        for (Protector protector: minions) {
//            protector.protect(booty);
//        }
//
//        if (enemies.size() > 0) {
//            for (GameObject enemy: enemies) {
//                if (targeted.size() >= targetedEnemies) {
//                    break;
//                }
//
//                targeted.add(enemy);
//            }
//
//            int index = 0;
//            List<GameObject> inList = new ArrayList<>(targeted);
//            for (Protector protector: minions) {
//                protector.aimFor(inList.get(index));
//                index = (index + 1) % inList.size();
//            }
//        }
//    }

}

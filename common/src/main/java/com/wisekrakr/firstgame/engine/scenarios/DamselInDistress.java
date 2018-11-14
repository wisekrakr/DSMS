package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.GameHelper;

import com.wisekrakr.firstgame.engine.gamecharacters.*;

import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;

import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectEvictionPolicy;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;


import java.util.*;

public class DamselInDistress extends Scenario {

    enum ScenarioState {
        INITIATION,
        PERVERTS,
        ESCORT
    }

    private ScenarioState state = ScenarioState.INITIATION;

    private DamselCharacter damsel;
    private Set<PervertCharacter> perverts = new HashSet<>();
    private Set<DamselCharacter> damselCharacters = new HashSet<>();
    private float runToDistance;
    private float escapeDistance;
    private final int maxPerverts;
    private Set<MissionEnding> missionEndings = new HashSet<>();
    private int numOfEndings = 0;

    public DamselInDistress(float runToDistance, float escapeDistance, int maxPerverts) {
        this.runToDistance = runToDistance;
        this.escapeDistance = escapeDistance;
        this.maxPerverts = maxPerverts;

    }

    @Override
    public void periodicUpdate() {

        switch (state) {
            case INITIATION:
                initiate();
                break;
            case PERVERTS:
                pervertUpdate();
                break;
            case ESCORT:
                missionEnding();
                break;
            default:
                throw new IllegalStateException("Unknown: " + state);
        }
    }

    private void initiate(){
        if (damselCharacters.isEmpty()) {
            damsel = new DamselCharacter(
                    getContext().space().chooseCreationPoint(),
                    50f,
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(100f, 150f),
                    runToDistance,
                    escapeDistance
            );
            damselCharacters.add(damsel);
            getContext().engine().addGameCharacter(damsel,
                    new GameCharacterListener() {
                        @Override
                        public void removed(GameCharacter target) {
                            damselCharacters.remove(damsel);
                        }
                    });
            System.out.println("Damsel added"); //todo remove
        }else {
            state = ScenarioState.PERVERTS;
        }
    }

    private void pervertUpdate(){

        if (perverts.size() < maxPerverts){
            GameCharacter pervert = new PervertCharacter(getContext().space().chooseCreationPoint(),
                    10f,
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(100f, 130f),
                    400f
            );

            perverts.add((PervertCharacter) pervert);
            getContext().engine().addGameCharacter(pervert,
                    new GameCharacterListener() {
                        @Override
                        public void removed(GameCharacter target) {
                            perverts.remove(pervert);
                        }
                    });
            System.out.println("Perverts added: " + perverts.size()); //todo remove
        }
        if (damselCharacters.isEmpty()){
            state = ScenarioState.INITIATION;
        }

//        if (DamselCharacter.DamselHelper.checkDamsel()) {
//            state = ScenarioState.ESCORT;
//            System.out.println("damsel is following player");
//        }
    }

    private void missionEnding(){

            if (missionEndings.isEmpty()){

                System.out.println("Adding a mission end");

                MissionEnding missionEnd = new MissionEnding(getContext().space().chooseCreationPoint(), 100f);

//                getContext().engine().addGameCharacter(missionEnd,
//                        new GameCharacterListener() {
//                            @Override
//                            public void removed(GameCharacter target) {
//                                if (!(DamselCharacter.DamselHelper.checkDamsel()) && missionEndings.size() > 1) {
//                                    missionEnd.getMissionEndContext().removeMyself();
//                                    state = ScenarioState.PERVERTS;
//                                }else if (DamselCharacter.DamselHelper.checkDamsel() && missionEndings.size() > 1){
//                                    missionEndings.remove(missionEnd);
//                                }
//                            }
//                        });
                missionEndings.add(missionEnd);
            }
    }

    private class MissionEnding extends AbstractGameCharacter{

        private Vector2 initialPosition;
        private float radius;

        private MissionEnding(Vector2 initialPosition, float radius) {
            this.initialPosition = initialPosition;
            this.radius = radius;
        }

        @Override
        public void start() {
            PhysicalObject missionEnd = getContext().addPhysicalObject(
                    "missionEnd",
                    initialPosition,
                    0,
                    0,
                    0,
                    Visualizations.B,
                    radius,
                    null,
                    PhysicalObjectEvictionPolicy.SLOW);

            getContext().updatePhysicalObjectExtra(missionEnd, "radius", radius);
        }

        @Override
        public void elapseTime(float delta) {
            new AbstractBehavior(){
                @Override
                public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                    if (object.getTags().contains(Tags.DAMSEL)){
                        MissionEnding.this.getContext().removeMyself();
                        DamselInDistress.this.stop();
                    }
                }
            };
        }
        public GameCharacterContext getMissionEndContext(){
            return getContext();
        }
    }
}

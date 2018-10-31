package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameEngine;
import com.wisekrakr.firstgame.engine.GameHelper;

import com.wisekrakr.firstgame.engine.gamecharacters.AbstractGameCharacter;

import com.wisekrakr.firstgame.engine.gamecharacters.DamselCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.PervertCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;

import com.wisekrakr.firstgame.engine.gameobjects.Player;
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
    public void start() {
        damsel = new DamselCharacter(GameHelper.randomPosition(),
                20f,
                GameHelper.randomDirection(),
                GameHelper.generateRandomNumberBetween(10f, 30f),
                400f,
                100f);


        getContext().engine().addGameCharacter(damsel, null);  // TODO: implement a listener
        state = ScenarioState.INITIATION;
    }

    @Override
    public void periodicUpdate() {
        switch (state) {
            case INITIATION:
                initiate(getContext().engine());
                System.out.println("Initiation " + damsel.damselContext().getPhysicalObject().getPosition());
                break;
            case PERVERTS:
                updatePervertsAndDamsel();
                //System.out.println("Placing perverts " + perverts.size());
                break;
            case ESCORT:
                bringDamselToSafeLocation(getContext().engine());
                System.out.println("Damsel found Hero " );
                break;
            default:
                throw new IllegalStateException("Unknown: " + state);
        }
    }

    private void initiate(GameEngine gameEngine){

        if (perverts.size() < maxPerverts){
            GameCharacter pervert = new PervertCharacter(GameHelper.randomPosition(),
                    10f,
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(30f, 50f),
                    200f,
                    30f);

            perverts.add((PervertCharacter) pervert);
            gameEngine.addGameCharacter(pervert, null); // TODO: implement listener
        }

        state = ScenarioState.PERVERTS;
    }


    private void updatePervertsAndDamsel(){

        List<NearPhysicalObject> nearbyPhysicalObjects =
                damsel.damselContext().findNearbyPhysicalObjects(damsel.damselContext().getPhysicalObject(), escapeDistance);

        if (!nearbyPhysicalObjects.isEmpty()) {
            for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                PhysicalObject target = nearPhysicalObject.getObject();

                float angle = GameHelper.angleBetween(damsel.damselContext().getPhysicalObject().getPosition(), target.getPosition());

                String name = target.getName();

                if (target.getClass().getClass() == Player.class.getClass()) {

                    if (GameHelper.distanceBetweenPhysicals(damsel.damselContext().getPhysicalObject(), target) < runToDistance) {
                        //damsel.clingOn();
                        state = ScenarioState.ESCORT;
                    }
                }else if (target == perverts.iterator().next()){

                    if (GameHelper.distanceBetweenPhysicals(damsel.damselContext().getPhysicalObject(), target) < escapeDistance) {

                    }
                }
            }
        }else {
            damsel.lookingForAHero();
        }

        for (PervertCharacter pervert: perverts){
            List<NearPhysicalObject> n =
                    pervert.getPervertContext().findNearbyPhysicalObjects(pervert.getPervertContext().getPhysicalObject(), 200f);

            if (!n.isEmpty()){
                for (NearPhysicalObject nearPhysicalObject: n){

                    PhysicalObject target = nearPhysicalObject.getObject();

                    if (target == damsel){
                        //pervert.addToBehaviorList(pervert.chaseAfter());
                    }else if (GameHelper.distanceBetweenPhysicals(pervert.getPervertContext().getPhysicalObject(), damsel.damselContext().getPhysicalObject())< 100f){
                        //pervert.addToBehaviorList(pervert.circlingDamsel());
                    }
                }
            }
        }

    }

    private void bringDamselToSafeLocation(GameEngine gameEngine){
        if (numOfEndings == 0){
            numOfEndings++;
        }

        if (missionEndings.size() < numOfEndings){
            System.out.println("Adding a mission end");

            MissionEnding missionEnd = new MissionEnding(GameHelper.randomPosition(), 50f);

            gameEngine.addGameCharacter(missionEnd, null);
            missionEndings.add(missionEnd);
            numOfEndings--;
        }
    }

//TODO: add characters like this?
    private class MissionEnding extends AbstractGameCharacter{

        private Vector2 initialPosition;
        private float radius;

        public MissionEnding(Vector2 initialPosition, float radius) {
            this.initialPosition = initialPosition;
            this.radius = radius;
        }

        @Override
        public void start() {
            PhysicalObject missionEnd = getContext().addPhysicalObject("missionEnd",
                    initialPosition,
                    0,
                    0,
                    0,
                    Visualizations.RIGHT_CANNON,
                    radius,
                    null, PhysicalObjectEvictionPolicy.DISCARD);
            getContext().updatePhysicalObjectExtra(missionEnd, "radius", radius);
        }

        @Override
        public void elapseTime(float delta) {
            new AbstractBehavior(){
                @Override
                public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                    if (damsel.damselContext().getPhysicalObject() == object){
                        MissionEnding.this.getContext().removeMyself();
                        damsel.missionComplete();
                    }
                }
            };
        }
    }
}

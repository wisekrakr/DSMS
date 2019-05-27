package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class PervertCharacter extends AbstractNonPlayerGameCharacter {

    private Vector2 initialPosition;
    private float initialRadius;
    private float initialDirection;
    private float initialSpeedMagnitude;
    private float radiusOfAttack;

    public PervertCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
    }

    @Override
    public void start() {
        BehavedObject pervert = introduceBehavedObject(
                "dirty rotten pervert",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.COCKPIT,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        getContext().removeMyself();
                    }
                });


        getContext().tagPhysicalObject(pervert.getObject(), Tags.PERVERT);

        AbstractNPCTools tools = getContext().npcTools(pervert.getObject());

        tools.tools().addTargetName(Tags.DAMSEL);

        tools.tools().healthIndicator(150f);

        pervert.behave(
                Arrays.asList(
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);

                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {

                                if (!object.getTags().contains(Tags.DEBRIS)) {
                                    getContext().updatePhysicalObject(null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null
                                    );
                                    tools.tools().damageIndicator(impact);
                                }
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                if (tools.tools().getHealth() <= 0){
                                    PervertCharacter.this.getContext().removeMyself();

                                    getContext().addCharacter(new ExplosionCharacter(
                                                    getContext().getSubject().getPosition(),
                                                    GameHelper.generateRandomNumberBetween(5f, 20f),
                                                    GameHelper.randomDirection(),
                                                    5,
                                                    getContext().getSubject().getCollisionRadius() * 2,
                                                    2f,
                                                    Visualizations.EXPLOSION),
                                                    new GameCharacterListener() {
                                                        @Override
                                                        public void removed(GameCharacter target) {
                                                            target.stop();
                                                        }
                                                    });
                                }
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(80f, 130f),
                                initialSpeedMagnitude
                        ),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, //todo: create a special behavior for perverts
                                radiusOfAttack,
                                initialSpeedMagnitude + GameHelper.generateRandomNumberBetween(10f, 50f),
                                getContext(),
                                tools.tools().targetList(getContext())
                        )

                ));
    }

}

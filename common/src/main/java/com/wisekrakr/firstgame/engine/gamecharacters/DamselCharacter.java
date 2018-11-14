package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;

import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class DamselCharacter extends AbstractNonPlayerGameCharacter {


    private Vector2 initialPosition;
    private float initialRadius;
    private float initialDirection;
    private float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float radiusOfEscape;

    public DamselCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float radiusOfEscape) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.radiusOfEscape = radiusOfEscape;
    }

    @Override
    public void start() {
        BehavedObject damsel = introduceBehavedObject(
                "little helpless spaceship",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.LEFT_CANNON,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        getContext().removeMyself();
                    }
                }
        );

        getContext().tagPhysicalObject(damsel.getObject(), Tags.DAMSEL);
        getContext().tagPhysicalObject(damsel.getObject(), Tags.FRIENDLY);

        AbstractNPCTools tools = getContext().npcTools(damsel.getObject());

        tools.tools().addAvoidName(Tags.PERVERT);
        tools.tools().addAvoidName(Tags.ATTACKER);
        tools.tools().addTargetName(Tags.PLAYER);

        tools.tools().healthIndicator(300f);

        damsel.behave(
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
                                    DamselCharacter.this.getContext().removeMyself();

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
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(60f, 180f),
                                initialSpeedMagnitude
                        ),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW,
                                radiusOfAttack,
                                initialSpeedMagnitude + GameHelper.generateRandomNumberBetween(20f, 40f),
                                getContext(),
                                tools.tools().targetList(getContext())
                        ),
                        new FlightBehavior(FlightBehavior.FlightStyle.FLY_AWAY,
                                radiusOfEscape,
                                initialSpeedMagnitude + GameHelper.generateRandomNumberBetween(30f, 50f),
                                getContext(),
                                tools.tools().avoidList(getContext()))

                ));
    }


}

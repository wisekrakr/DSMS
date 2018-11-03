package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class NPCAvoiding extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;

    public NPCAvoiding(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
    }

    @Override
    public void start() {

        BehavedObject npcNewbie = introduceBehavedObject(
                "little bitch",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.COCKPIT,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        NPCAvoiding.this.getContext().removeMyself();
                    }
                });

        getContext().tagPhysicalObject(npcNewbie.getObject(), Tags.DODGER);
        getContext().tagPhysicalObject(npcNewbie.getObject(), Tags.FRIENDLY);

        AbstractNPCTools tools = new AbstractNPCTools();

        tools.tools().addAvoidName(Tags.ATTACKER);
        tools.tools().addAvoidName(Tags.PROJECTILE);
        tools.tools().addTargetName(Tags.PLAYER);

        npcNewbie.behave(
                Arrays.asList(
                        new AbstractBehavior(){
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
                                            null
                                    );
                                }
                            }
                        },
                        new CruisingBehavior(5f, initialSpeedMagnitude),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, initialSpeedMagnitude + GameHelper.generateRandomNumberBetween(30f, 60f), getContext(), tools.tools().targetList(NPCAvoiding.this.getContext())),
                        new FlightBehavior(FlightBehavior.FlightStyle.FLY_AWAY, radiusOfAttack/2, initialSpeedMagnitude + GameHelper.generateRandomNumberBetween(30f, 60f), getContext(), tools.tools().avoidList(NPCAvoiding.this.getContext()))

                ));
    }
}





package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class NPCPlayerHunter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;


    public NPCPlayerHunter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;

    }

    @Override
    public void start() {
        BehavedObject playerHunter = introduceBehavedObject(
                "player hunter",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.A,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        NPCPlayerHunter.this.getContext().removeMyself();
                    }
                });

        getContext().tagPhysicalObject(playerHunter.getObject(), Tags.ATTACKER);
        getContext().tagPhysicalObject(playerHunter.getObject(), Tags.BULLET_ATTACKER);

        AbstractNPCTools tools = getContext().npcTools(playerHunter.getObject());

        tools.tools().addTargetName(Tags.PLAYER);

        playerHunter.behave(
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
                                            null,
                                            null
                                    );
                                }
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                tools.tools().weaponry(NPCPlayerHunter.this.getContext(), CharacterTools.Weaponry.SPLASH_BULLETS, 1f, radiusOfAttack);
                                tools.tools().weaponry(NPCPlayerHunter.this.getContext(), CharacterTools.Weaponry.HOMING_MISSILES, 0.7f, radiusOfAttack-200f);
                                tools.tools().weaponry(NPCPlayerHunter.this.getContext(), CharacterTools.Weaponry.BULLETS, 0.4f, radiusOfAttack-400f);

                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f), initialSpeedMagnitude),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW,
                                radiusOfAttack,
                                initialSpeedMagnitude + GameHelper.generateRandomNumberBetween(30f, 60f),
                                getContext(),
                                tools.tools().targetList(NPCPlayerHunter.this.getContext()))

                ));
    }


}





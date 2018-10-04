package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.MiscAttackBehaviors;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;
import java.util.List;

public class StandardAggressiveCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float lastShot;
    private Float fireRate = 1f;

    public StandardAggressiveCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
    }


    @Override
    public void start() {
        BehavedObject attackerA = introduceBehavedObject("Attacker A",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius);

        attackerA.behave(
                Arrays.asList(
                        new CruisingBehavior(5f, initialSpeedMagnitude),
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                                getContext().updatePhysicalObjectExtra("health", 10d);
                                getContext().updatePhysicalObjectExtra("maxHealth", 100d);
                                getContext().updatePhysicalObjectExtra("healthPercentage", 1d);

                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                float radiusOfAttack = 500f;

                                List<NearPhysicalObject> nearbyPhysicalObjects =
                                        StandardAggressiveCharacter.this.getContext().findNearbyPhysicalObjects(StandardAggressiveCharacter.this.getContext().getPhysicalObject(), radiusOfAttack);

                                if (!nearbyPhysicalObjects.isEmpty()) {
                                    for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                                        String name = nearPhysicalObject.getObject().getName();

                                        if (!name.contains("weapon")) {

                                            if (GameHelper.distanceBetweenPhysicals(StandardAggressiveCharacter.this.getContext().getPhysicalObject(), nearPhysicalObject.getObject()) < radiusOfAttack / 2) {
                                                MiscAttackBehaviors.chasingAndShooting(StandardAggressiveCharacter.this.getContext(), nearPhysicalObject.getObject(), 1f, clock);

                                            } else {
                                                MiscAttackBehaviors.chase(StandardAggressiveCharacter.this.getContext(), nearPhysicalObject.getObject(), initialSpeedMagnitude + 30f);
                                            }
                                        }
                                    }
                                }
                            }
                        }

        ));
    }

}





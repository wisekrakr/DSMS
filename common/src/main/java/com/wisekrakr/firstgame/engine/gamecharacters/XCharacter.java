package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.*;
import com.wisekrakr.firstgame.engine.physicalobjects.*;

import java.util.*;

public class XCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float lastShot;
    private Float fireRate = 1f;

    public XCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
    }


    @Override
    public void start() {
        BehavedObject middle = introduceBehavedObject("Test Physical Object",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius);




        middle.behave(
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
                                        XCharacter.this.getContext().findNearbyPhysicalObjects(XCharacter.this.getContext().getPhysicalObject(), radiusOfAttack);

                                if (!nearbyPhysicalObjects.isEmpty()) {
                                    for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                                        String name = nearPhysicalObject.getObject().getName();

                                        if (!name.contains("weapon")) {

                                            if (GameHelper.distanceBetweenPhysicals(XCharacter.this.getContext().getPhysicalObject(), nearPhysicalObject.getObject()) < radiusOfAttack / 2) {
                                                MiscAttackBehaviors.chasingAndShooting(XCharacter.this.getContext(), nearPhysicalObject.getObject(), 1f, clock);


                                            } else {
                                                MiscAttackBehaviors.chase(XCharacter.this.getContext(), nearPhysicalObject.getObject(), initialSpeedMagnitude + 30f);
                                            }
                                        }
                                    }
                                }
                            }
                        }

        ));
    }

}





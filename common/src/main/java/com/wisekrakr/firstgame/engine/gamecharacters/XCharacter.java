package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.*;
import com.wisekrakr.firstgame.engine.physicalobjects.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private Set<PhysicalObject> parts = new HashSet<>();
    private int numOfParts = 8;

    public XCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
    }

    @Override
    public void start() {
        BehavedObject middle = introduceBehavedObject("Middle",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius);

        middle.behave(
                Arrays.asList(
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                List<NearPhysicalObject> nearbyPhysicalObjects = XCharacter.this.getContext().findNearbyPhysicalObjects(getContext().getSubject(), 2000f);

                                if (!nearbyPhysicalObjects.isEmpty()) {
                                    System.out.println("Boom (TODO)");
//                                    rootBehavior(new ExplodeAndLeaveDebrisBehavior(x, 20, 5, 50));
                                }

  //                              x = null;



                /*
                if (parts.size() < numOfParts){
                    PhysicalObject physicalObject = getContext().addPhysicalObject("Back",
                            new Vector2((float) (initialPosition.x + initialRadius * Math.cos(x.getOrientation() + Math.PI) / parts.size() * 2),
                                    (float) (initialPosition.y + initialRadius * Math.sin(x.getOrientation() + Math.PI ) / parts.size() * 2)),
                            x.getOrientation(),
                            x.getSpeedMagnitude(),
                            x.getSpeedDirection(),
                            Visualizations.REAR_BOOSTER,
                            x.getCollisionRadius(),
                            null);
                    parts.add(physicalObject);
                    getContext().updatePhysicalObjectExtra(physicalObject, "radius", physicalObject.getCollisionRadius()/3);
                }
                for (PhysicalObject physicalObject: parts){
                    getContext().updatePhysicalObject(physicalObject,
                            null,
                            null,
                            null,
                            x.getSpeedMagnitude(),
                            x.getSpeedDirection(),
                            null,
                            null);
                }
                */

                            }
                        }, new CruisingBehavior(5f))
        );


    }


}

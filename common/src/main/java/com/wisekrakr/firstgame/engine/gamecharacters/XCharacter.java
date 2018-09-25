package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.*;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.HashSet;
import java.util.Set;

public class XCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private Set<PhysicalObject>parts = new HashSet<>();
    private int numOfParts = 8;

    public XCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
    }

    @Override
    public void start() {
        rootBehavior(new AbstractBehavior(){

            private PhysicalObject x;

            @Override
            public void start() {
                x = getContext().addPhysicalObject("Middle",
                        initialPosition,
                        initialDirection,
                        initialSpeedMagnitude,
                        initialDirection,
                        Visualizations.TEST,
                        initialRadius,
                        new PhysicalObjectListener() {
                            @Override
                            public void collision(PhysicalObject two, float time, Vector2 epicentre, float impact) {
                                rootBehavior(new TurnBackBehavior(x, two, initialSpeedMagnitude));

                            }

                            @Override
                            public void nearby(PhysicalObject target, float time, Vector2 position) {

                            }
                        });

                getContext().updatePhysicalObjectExtra(x, "radius", initialRadius);
                rootBehavior(new CruisingBehavior(x, 5f));

            }

            @Override
            public void elapseTime(float clock, float delta) {



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
        });

    }


}

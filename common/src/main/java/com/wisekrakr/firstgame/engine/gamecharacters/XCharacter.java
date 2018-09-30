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
                                getContext().updatePhysicalObjectExtra("health", 10d);
                                getContext().updatePhysicalObjectExtra("maxHealth", 100d);
                                getContext().updatePhysicalObjectExtra("healthPercentage", 1d);

                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                List<NearPhysicalObject>nearbyPhysicalObjects = XCharacter.this.getContext().findNearbyPhysicalObjects(getContext().getSubject(), 300f);

                                if (!nearbyPhysicalObjects.isEmpty()) {
                                    for (NearPhysicalObject nearPhysicalObject: nearbyPhysicalObjects){
                                        float angle = GameHelper.angleBetween(getContext().getSubject().getPosition(), nearPhysicalObject.getObject().getPosition());
                                        getContext().updatePhysicalObject(
                                                null, null,
                                                angle,
                                                initialSpeedMagnitude,
                                                angle,
                                                null, null);
                                        System.out.println("target: " + nearPhysicalObject.getObject().getName());
                                    }
                                }
                            }
                        }, new AbstractBehavior()
        ));
    }

}



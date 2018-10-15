package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

import java.util.List;

public class FlightBehavior extends AbstractBehavior {

    private float radiusOfAttack;
    private Float speedIncrease;
    private GameCharacterContext master;
    private FlightStyle flightStyle;

    public FlightBehavior(FlightStyle flightStyle, float radiusOfAttack, Float speedIncrease, GameCharacterContext master) {
        this.flightStyle = flightStyle;
        this.radiusOfAttack = radiusOfAttack;
        this.speedIncrease = speedIncrease;
        this.master = master;
    }

    public enum FlightStyle {
        FOLLOW, FACEHUG, CIRCLING, FLY_AWAY
    }

    @Override
    public void elapseTime(float clock, float delta) {

        List<NearPhysicalObject> nearbyPhysicalObjects =
                master.findNearbyPhysicalObjects(getContext().getSubject(), radiusOfAttack);

        if (!nearbyPhysicalObjects.isEmpty()) {
            for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                PhysicalObject target = nearPhysicalObject.getObject();

                float angle = GameHelper.angleBetween(getContext().getSubject().getPosition(), target.getPosition());

                String name = target.getName();

                if (!name.contains("weapon") && !name.contains("debris") && target != getContext().getSubject() &&
                        !name.contains(getContext().getSubject().getName()) && target != master.getPhysicalObject()) {

                    if (GameHelper.distanceBetweenPhysicals(getContext().getSubject(), target) < radiusOfAttack) {

                        switch (flightStyle){
                            case FOLLOW:
                                //angle towards target and start following

                                getContext().updatePhysicalObject(
                                        null,
                                        null,
                                        angle,
                                        speedIncrease,
                                        angle,
                                        null,
                                        null,
                                        null,
                                        null
                                );
                                System.out.println(getContext().getSubject().getName() + " chasing target: " + target.getName());
                                break;
                            case FACEHUG:
                                //angle towards target and when close enough cling to target from short distance

                                float rotationAngle = 3f * delta * MathUtils.PI;

                                getContext().updatePhysicalObject(
                                        null,
                                        new Vector2(getContext().getSubject().getPosition().x + target.getCollisionRadius() + getContext().getSubject().getCollisionRadius() + MathUtils.cos(rotationAngle),
                                                getContext().getSubject().getPosition().y + target.getCollisionRadius() + getContext().getSubject().getCollisionRadius() * MathUtils.sin(rotationAngle)),
                                        angle,
                                        null,
                                        angle + rotationAngle,
                                        null,
                                        null,
                                        null,
                                        null
                                );
                                System.out.println(getContext().getSubject().getName() + " = facehugging target: " + target.getName());

                                break;
                            case CIRCLING:
                                //angle towards target and when close start circling around the target (then shoot or protect or nothing)

                                float updatedAngle = (float) (45f * Math.PI * delta);

                                getContext().updatePhysicalObject(
                                        null,
                                        null,
                                        angle,
                                        null,
                                        angle + updatedAngle,
                                        null,
                                        null,
                                        null,
                                        null
                                );

                                System.out.println(getContext().getSubject().getName() + " = circling target: " + target.getName());

                                break;
                            case FLY_AWAY:

                                getContext().updatePhysicalObject(
                                        null,
                                        null,
                                        -angle,
                                        speedIncrease,
                                        -angle,
                                        null,
                                        null,
                                        null,
                                        null
                                        );
                                System.out.println(getContext().getSubject().getName() + " = running from: " + target.getName());

                                break;
                            default:
                                System.out.println("No Flight Behavior chosen for : " + getContext().getSubject().getName());

                        }
                    }
                }
            }
        }
    }

}

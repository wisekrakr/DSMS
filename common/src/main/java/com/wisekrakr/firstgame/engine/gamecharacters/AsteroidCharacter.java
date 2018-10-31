package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.RotatingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class AsteroidCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private final float health;
    private final float damage;

    public AsteroidCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float health, float damage) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.health = health;
        this.damage = damage;
    }

    @Override
    public void start() {
        BehavedObject behavedObject = introduceBehavedObject(
                AsteroidCharacter.class.getName(),
                initialPosition,
                0,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.BOULDER,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        System.out.println("Asteroid character is a goner");
                        AsteroidCharacter.this.getContext().removeMyself();
                    }
                });

        behavedObject.behave(Arrays.asList(
                new AbstractBehavior() {
                    @Override
                    public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                        getContext().addCharacter(new ExplosionCharacter(object.getPosition(),
                                GameHelper.generateRandomNumberBetween(5f, initialSpeedMagnitude),
                                GameHelper.randomDirection(),
                                5,
                                initialRadius,
                                10f,
                                Visualizations.BOULDER), null);

                        // TODO: should happen automatically
                        getContext().removePhysicalObject();
                        AsteroidCharacter.this.getContext().removeMyself();
                    }

                    @Override
                    public void start() {
                        getContext().updatePhysicalObjectExtra("radius", initialRadius);
                    }

                    @Override
                    public void elapseTime(float clock, float delta) {
                        //getContext().updatePhysicalObject(null, null, null, Math.min(100f, 10 * clock), Math.min(clock, 1000f), null, null, null, null);
/*
                        List<NearPhysicalObject> nearbyPhysicalObjects = AsteroidCharacter.this.getContext().findNearbyPhysicalObjects(getContext().getSubject(), 300f);

                        if (!nearbyPhysicalObjects.isEmpty()) {
                            for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {
                                float angle = GameHelper.angleBetween(getContext().getSubject().getPosition(), nearPhysicalObject.getObject().getPosition());

                                getContext().updatePhysicalObject(
                                        null,
                                        null,
                                        -angle,
                                        initialSpeedMagnitude,
                                        -angle,
                                        null,
                                        null,
                                        null,
                                        null);
                            }
                        }
*/
                        if (getContext().getSubject().getCollisionRadius() <= 0.5f) {
                            getContext().removePhysicalObject();
                        }
                    }
                }, new RotatingBehavior(GameHelper.generateRandomNumberBetween(5f, 20f))
        ));

    }


}

package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.RotatingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class AsteroidCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;

    public AsteroidCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
    }

    @Override
    public void start() {
        BehavedObject behavedObject = introduceBehavedObject(
                "A",
                initialPosition,
                0,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.BOULDER,
                initialRadius);

        behavedObject.behave(Arrays.asList(
                new AbstractBehavior() {
                    @Override
                    public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                        getContext().addCharacter(new ExplosionCharacter(object.getPosition(),
                                object.getSpeedMagnitude(),
                                object.getSpeedDirection(),
                                10,
                                initialRadius,
                                10f,
                                Visualizations.BOULDER));

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
                        //getContext().updatePhysicalObject(physicalObject, null, null, null, Math.min(100f, 10 * clock), Math.min(clock, 1000f), null, null);

                        //getContext().pushSubBehavior(new RotatingBehavior(physicalObject, GameHelper.generateRandomNumberBetween(5f, 25f)));

                        if (getContext().getSubject().getCollisionRadius() <= 0.5f) {
                            getContext().removePhysicalObject();
                        }
                    }
                },
                new RotatingBehavior(5f)));
    }
}

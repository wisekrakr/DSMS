package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.RotatingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

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
        rootBehavior(new AbstractBehavior() {
            private PhysicalObject physicalObject;

            @Override
            public void start() {
                physicalObject = getContext().addPhysicalObject(
                        "A",
                        initialPosition,
                        0,
                        initialSpeedMagnitude,
                        initialDirection,
                        Visualizations.BOULDER,
                        initialRadius,
                        new PhysicalObjectListener() {
                            @Override
                            public void collision(PhysicalObject two, float time, Vector2 epicentre, float impact) {
                                getContext().addCharacter(new ExplosionCharacter(physicalObject.getPosition(),
                                        physicalObject.getSpeedMagnitude(),
                                        physicalObject.getSpeedDirection(),
                                        10,
                                        initialRadius,
                                        10f,
                                        Visualizations.BOULDER));

                                // TODO: should happen automatically
                                getContext().removePhysicalObject(physicalObject);
                                AsteroidCharacter.this.getContext().removeMyself();

                            }

                            @Override
                            public void nearby(PhysicalObject target, float time, Vector2 position) {

                            }
                        });

                getContext().updatePhysicalObjectExtra(physicalObject, "radius", initialRadius);

                rootBehavior(new RotatingBehavior(physicalObject, 25f));


            }

            @Override
            public void elapseTime(float clock, float delta) {
                //getContext().updatePhysicalObject(physicalObject, null, null, null, Math.min(100f, 10 * clock), Math.min(clock, 1000f), null, null);

                //getContext().pushSubBehavior(new RotatingBehavior(physicalObject, GameHelper.generateRandomNumberBetween(5f, 25f)));

                if (this.physicalObject.getCollisionRadius() <= 0.5f) {
                    getContext().removePhysicalObject(physicalObject);
                }
            }
        });
    }
}

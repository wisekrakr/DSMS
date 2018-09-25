package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.ChasingBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.RotatingBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectSnapshot;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.HashSet;
import java.util.Set;

public class SnakeCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private PhysicalObject x;
    private Set<PhysicalObject>parts = new HashSet<>();
    private int numOfParts;

    public SnakeCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, int numOfParts) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.numOfParts = numOfParts;
    }

    @Override
    public void start() {
        x = getContext().addPhysicalObject("Middle",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius ,
                null);

        getContext().updatePhysicalObjectExtra(x, "radius", initialRadius);
    }

    @Override
    public void elapseTime(float delta) {
        //rootBehavior(new CruisingBehavior(x, 5f));

        if (parts.size() < numOfParts){
            PhysicalObject physicalObject = getContext().addPhysicalObject("Back",
                    new Vector2((float) (initialPosition.x + initialRadius * Math.cos(x.getOrientation() + Math.PI) * parts.size()),
                            (float) (initialPosition.y + initialRadius * Math.sin(x.getOrientation() + Math.PI ) * parts.size())),
                    x.getOrientation(),
                    x.getSpeedMagnitude(),
                    x.getSpeedDirection(),
                    Visualizations.PLAYER,
                    x.getCollisionRadius(),
                    null);
            parts.add(physicalObject);
            getContext().updatePhysicalObjectExtra(physicalObject, "radius", physicalObject.getCollisionRadius());

        }

    }
}

package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.ArrayList;
import java.util.List;

public class ExplosionCharacter extends AbstractGameCharacter {

    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private int debrisParts;
    private float debrisMass;
    private float debrisAge;
    private Visualizations visualizations;
    private List<PhysicalObject> bits = new ArrayList<>();

    public ExplosionCharacter(Vector2 position, float speedMagnitude, float speedDirection, int debrisParts, float debrisMass, float debrisAge, Visualizations visualizations) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.debrisParts = debrisParts;
        this.debrisMass = debrisMass;
        this.debrisAge = debrisAge;
        this.visualizations = visualizations;
    }

    @Override
    public void start() {
        float bitSize = (float) Math.sqrt((debrisMass * debrisMass) / debrisParts);
        for (int i = 0; i < debrisParts; i++) {
            PhysicalObject bit = getContext().addPhysicalObject("debris",
                    position,
                    GameHelper.randomDirection(),
                    speedMagnitude,
                    GameHelper.randomDirection(),
                    0,
                    0,
                    visualizations,
                    bitSize,
                    null
            );

            getContext().updatePhysicalObjectExtra(bit, "radius", bitSize);

            bits.add(bit);
        }
    }

    @Override
    public void elapseTime(float delta) {
        debrisAge = debrisAge - delta;
        if (debrisAge < 0) {
            for (PhysicalObject object: bits) {
                getContext().removePhysicalObject(object);
            }

            getContext().removeMyself();
        }

    }


}

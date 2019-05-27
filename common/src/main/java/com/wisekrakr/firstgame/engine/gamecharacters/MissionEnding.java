package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectEvictionPolicy;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

public class MissionEnding extends AbstractGameCharacter {

    private Vector2 initialPosition;
    private float radius;

    public MissionEnding(Vector2 initialPosition, float radius) {
        this.initialPosition = initialPosition;
        this.radius = radius;
    }

    @Override
    public void start() {
        PhysicalObject missionEnd = getContext().addPhysicalObject(
                "missionEnd",
                initialPosition,
                0,
                0,
                0,
                Visualizations.B,
                radius,
                null,
                PhysicalObjectEvictionPolicy.SLOW);

        getContext().updatePhysicalObjectExtra(missionEnd, "radius", radius);
        getContext().tagPhysicalObject(missionEnd, Tags.MISSION_END);
    }

    @Override
    public void elapseTime(float delta) {
        new AbstractBehavior(){
            @Override
            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                if (object.getTags().contains(Tags.DAMSEL)){
                    MissionEnding.this.getContext().removeMyself();
                    this.stop();
                }
            }
        };
    }

}

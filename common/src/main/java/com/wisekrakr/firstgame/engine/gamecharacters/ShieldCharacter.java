package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectEvictionPolicy;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

public class ShieldCharacter extends AbstractGameCharacter {

    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private float radius;
    private float health;
    private Float minusInHundredsShieldTime;
    private Visualizations visualizations;
    private GameCharacterContext master;
    private PhysicalObject shield;

    public ShieldCharacter(Vector2 position, float speedMagnitude, float speedDirection, float radius, float health, Float minusInHundredsShieldTime, Visualizations visualizations, GameCharacterContext master) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.radius = radius;
        this.health = health;
        this.minusInHundredsShieldTime = minusInHundredsShieldTime;
        this.visualizations = visualizations;
        this.master = master;
    }

    @Override
    public void start() {

        shield = getContext().addPhysicalObject("shield",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                visualizations,
                radius,
                null,
                PhysicalObjectEvictionPolicy.DISCARD);
        getContext().updatePhysicalObjectExtra(shield, "radius", radius);
        getContext().tagPhysicalObject(shield, Tags.DEFENSE_MATERIAL);
    }

    @Override
    public void elapseTime(float delta) {

        //System.out.println(StringHelper.ANSI_CYAN_BACKGROUND + "SHIELDS UP!" + StringHelper.ANSI_RESET);


        if (master != null && getContext() != null) {
            if (getContext().getSpaceEngine().getTime() - minusInHundredsShieldTime > Math.abs(minusInHundredsShieldTime)/100) {
                        /*
                switch(onOrOff)
                 */

                getContext().updatePhysicalObject(shield,
                        null,
                        master.getPhysicalObject().getPosition(),
                        master.getPhysicalObject().getOrientation(),
                        master.getPhysicalObject().getSpeedMagnitude(),
                        master.getPhysicalObject().getSpeedDirection(),
                        null,
                        null,
                        null
                );
                minusInHundredsShieldTime = getContext().getSpaceEngine().getTime();
            }else {
                getContext().removeMyself();
                getContext().removePhysicalObject(shield);
                //System.out.println(StringHelper.ANSI_PURPLE_BACKGROUND + "SHIELDS DOWN!" + StringHelper.ANSI_RESET);

            }

        }
    }

}

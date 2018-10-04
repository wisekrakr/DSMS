package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.MiscAttackBehaviors;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.RotatingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;
import java.util.List;

public class HomingMissileCharacter extends AbstractNonPlayerGameCharacter {


    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private final float bulletAge;
    private final int ammoCount;
    private final float radius;
    private final Visualizations visualizations;
    private GameCharacterContext master;


    public HomingMissileCharacter(Vector2 position, float speedMagnitude, float speedDirection, float bulletAge, int ammoCount, float radius, Visualizations visualizations, GameCharacterContext master) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.bulletAge = bulletAge;
        this.ammoCount = ammoCount;
        this.radius = radius;
        this.visualizations = visualizations;
        this.master = master;
    }


    @Override
    public void start() {
        BehavedObject missile = introduceBehavedObject("weapon",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                visualizations,
                radius);

        missile.behave(
                Arrays.asList(
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", radius);

                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                float radiusOfAttack = 100f;

                                List<NearPhysicalObject> nearbyPhysicalObjects =
                                        HomingMissileCharacter.this.getContext().findNearbyPhysicalObjects(HomingMissileCharacter.this.getContext().getPhysicalObject(), radiusOfAttack);

                                if (!nearbyPhysicalObjects.isEmpty()) {
                                    for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                                        String name = nearPhysicalObject.getObject().getName();

                                        if (!name.contains("weapon") && nearPhysicalObject.getObject() != master.getPhysicalObject()) {

                                            if (GameHelper.distanceBetweenPhysicals(HomingMissileCharacter.this.getContext().getPhysicalObject(), nearPhysicalObject.getObject()) < radiusOfAttack / 2) {
                                                MiscAttackBehaviors.chase(HomingMissileCharacter.this.getContext(), nearPhysicalObject.getObject(), 100f);

                                            }
                                        }
                                    }
                                }
                            }
                        }, new RotatingBehavior(10f)

        ));
    }

}





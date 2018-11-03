package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.Arrays;

public class NPCMissileShooter extends AbstractNonPlayerGameCharacter {

    private Vector2 initialPosition;
    private float initialRadius;
    private float initialDirection;
    private float initialSpeedMagnitude;
    private float radiusOfAttack;

    public NPCMissileShooter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack) {

        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;

    }

    @Override
    public void start() {
        BehavedObject npcMissileMain = introduceBehavedObject(
                "missile shooter",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.ENEMY,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        NPCMissileShooter.this.getContext().removeMyself();
                    }
                });

        getContext().tagPhysicalObject(npcMissileMain.getObject(), Tags.ATTACKER);
        getContext().tagPhysicalObject(npcMissileMain.getObject(), Tags.MISSILE_ATTACKER);

        AbstractNPCTools tools = getContext().npcTools(npcMissileMain.getObject());

        tools.tools().addTargetName(Tags.PLAYER);
        tools.tools().addTargetName(Tags.DODGER);

        npcMissileMain.behave(
                Arrays.asList(
                        new AbstractBehavior(){
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                if (!object.getTags().contains(Tags.DEBRIS)) {
                                    getContext().updatePhysicalObject(null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null
                                    );
                                }
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                tools.tools().weaponry(NPCMissileShooter.this.getContext(), CharacterTools.Weaponry.HOMING_MISSILES, 1.4f, radiusOfAttack);
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(30f, 120f), initialSpeedMagnitude),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW,
                                radiusOfAttack,
                                initialSpeedMagnitude,
                                getContext(),
                                tools.tools().targetList(NPCMissileShooter.this.getContext()))

        ));
    }
}





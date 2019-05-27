package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.DuplicationBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.Arrays;

public class NPCMinionSpawner extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;

    public NPCMinionSpawner(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;

    }


    @Override
    public void start() {
        BehavedObject duplicationStation = introduceBehavedObject(
                "mother ship",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius, null
        );

        getContext().tagPhysicalObject(duplicationStation.getObject(), Tags.MOTHER_SHIP);
        getContext().tagPhysicalObject(duplicationStation.getObject(), Tags.ATTACKER);
        getContext().tagPhysicalObject(duplicationStation.getObject(), Tags.SPAWNING_ATTACKER);

        AbstractNPCTools tools = getContext().npcTools(duplicationStation.getObject());

        tools.tools().addTargetName(Tags.FRIENDLY);
        tools.tools().addTargetName(Tags.PLAYER);

        tools.tools().healthIndicator(1000f);

        duplicationStation.behave(
                Arrays.asList(
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);

                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                if (!object.getTags().contains(Tags.DEBRIS) && !object.getTags().contains(Tags.MINION)) {
                                    getContext().updatePhysicalObject(null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null
                                    );
                                }
                            }


                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f), initialSpeedMagnitude),
                        new DuplicationBehavior(DuplicationBehavior.DuplicationStyle.DEPLOY_MINIONS, radiusOfAttack, 30, 0.5f, NPCMinionSpawner.this.getContext(), tools.tools().targetList(getContext()), new CharacterFactory() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                                return new NPCMinion(position,
                                        radius,
                                        speedDirection,
                                        speedMagnitude,
                                        radiusOfAttack,
                                        Visualizations.ENEMY,
                                        tools.tools().targetList(getContext()),
                                        NPCMinionSpawner.this.getContext());
                            }
                        }),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack / 2, 1, NPCMinionSpawner.this.getContext(), tools.tools().targetList(getContext()), new CharacterFactory<AbstractNonPlayerGameCharacter>() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                                return new HomingMissileCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        5f,
                                        3f,
                                        radiusOfAttack,
                                        Visualizations.RIGHT_CANNON,
                                        getContext(),
                                        tools.tools().targetList(getContext()));
                            }
                        })

                ));
    }

}



package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.PlayerCreationRequest;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.StringHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.DuplicationBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.Arrays;

public class NPCMinionSpawner extends AttackingCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;

    public NPCMinionSpawner(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }


    @Override
    public void start() {
        BehavedObject duplicationStation = introduceBehavedObject(NPCMinionSpawner.class.getName(),
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                health,
                0,
                Visualizations.TEST,
                initialRadius);

        addTargetName(PlayerCreationRequest.playerName());

        duplicationStation.behave(
                Arrays.asList(
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                                getContext().updatePhysicalObjectExtra("health", health);

                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                if (!object.getName().contains("debris") && !object.getName().contains(NPCMinion.class.getName())) {
                                    getContext().updatePhysicalObject(null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            health -= object.getDamage(),
                                            null,
                                            null,
                                            null
                                    );
                                }
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                if (health <= 0) {
                                    NPCMinionSpawner.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f), initialSpeedMagnitude),
                        new DuplicationBehavior(DuplicationBehavior.DuplicationStyle.DEPLOY_MINIONS, radiusOfAttack, 30, 0.5f, NPCMinionSpawner.this.getContext(), targetList(), new CharacterFactory() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                                return new NPCMinion(position,
                                        radius,
                                        speedDirection,
                                        speedMagnitude,
                                        radiusOfAttack,
                                        health,
                                        Visualizations.ENEMY,
                                        targetList(),
                                        NPCMinionSpawner.this.getContext());
                            }
                        }),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack / 2, 1, NPCMinionSpawner.this.getContext(), targetList(), new CharacterFactory<AbstractNonPlayerGameCharacter>() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                                return new HomingMissileCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        5f,
                                        getContext().getPhysicalObject().getCollisionRadius() * 2,
                                        3f,
                                        radiusOfAttack,
                                        Visualizations.RIGHT_CANNON,
                                        getContext(),
                                        targetList());
                            }
                        })

                ));
    }

}



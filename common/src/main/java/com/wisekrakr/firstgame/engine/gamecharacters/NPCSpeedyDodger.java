package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.DefenseBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.Arrays;

public class NPCSpeedyDodger extends AttackingCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;

    public NPCSpeedyDodger(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }

    @Override
    public void start() {
        BehavedObject speedy = introduceBehavedObject(AttackingCharacter.class.getName(),
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius, null);

        speedy.behave(
                Arrays.asList(
                        new AbstractBehavior(){
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                                getContext().updatePhysicalObjectExtra("health", health);

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
                                if (health <= 0){
                                    NPCSpeedyDodger.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f), initialSpeedMagnitude),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack +100f, initialSpeedMagnitude + 50f, getContext(), tools().targetList()),
                        new DefenseBehavior(DefenseBehavior.DefenseStyle.DODGE, radiusOfAttack +30f, initialSpeedMagnitude + 80f, null, getContext()),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack , 0.5f, NPCSpeedyDodger.this.getContext(), tools().targetList(), new CharacterFactory<AbstractNonPlayerGameCharacter>() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                                return new BulletCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        5f,
                                        3f,
                                        3f,
                                        Visualizations.EXPLOSION,
                                        getContext()
                                );
                            }
                        })
                ));
    }


}





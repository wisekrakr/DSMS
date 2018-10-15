package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.Arrays;

public class StandardAggressiveCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;

    public StandardAggressiveCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }


    @Override
    public void start() {
        BehavedObject attackerA = introduceBehavedObject("attacker A1",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                health,
                0,
                Visualizations.ENEMY,
                initialRadius);

        attackerA.behave(
                Arrays.asList(
                        new AbstractBehavior(){
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                                getContext().updatePhysicalObjectExtra("health", health);
                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                if (!object.getName().contains("debris")) {
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
                                if (health <= 0){
                                    StandardAggressiveCharacter.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new CruisingBehavior(5f, initialSpeedMagnitude),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack, 1f, getContext(), new CharacterFactory<AbstractNonPlayerGameCharacter>() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                                return new BulletCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        3f,
                                        radius,
                                        5f,
                                        Visualizations.LEFT_CANNON,
                                        getContext()
                                ) ;
                            }
                        }),
                        new FlightBehavior(FlightBehavior.FlightStyle.CIRCLING, radiusOfAttack/2, 0f, getContext())


        ));
    }

}





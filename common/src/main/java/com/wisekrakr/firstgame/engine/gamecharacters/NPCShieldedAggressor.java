package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.client.PlayerCreationRequest;
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

public class NPCShieldedAggressor extends AttackingCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;

    public NPCShieldedAggressor(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }

    @Override
    public void start() {
        BehavedObject shieldedAggressor = introduceBehavedObject(AttackingCharacter.class.getName(),
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                health,
                0,
                Visualizations.TEST,
                initialRadius);

        addTargetName(AttackingCharacter.class.getName());

        shieldedAggressor.behave(
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
                                    NPCShieldedAggressor.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f), initialSpeedMagnitude),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack +100f, initialSpeedMagnitude + 30f, getContext(), targetList()),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack , 0.5f, NPCShieldedAggressor.this.getContext(), targetList(), new CharacterFactory<AbstractNonPlayerGameCharacter>() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                                return new BulletCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        5f,
                                        3f,
                                        3f,
                                        Visualizations.BOULDER,
                                        getContext()
                                );
                            }
                        }),
                        new DefenseBehavior(DefenseBehavior.DefenseStyle.SHIELDS_UP, radiusOfAttack/3, null, -300f, getContext())

                ));
    }


}





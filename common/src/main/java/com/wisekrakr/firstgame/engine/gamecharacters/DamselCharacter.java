package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.Behavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;

import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DamselCharacter extends FriendlyCharacter {

    private Vector2 initialPosition;
    private float initialRadius;
    private float initialDirection;
    private float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;
    private Behavior desiredBehavior;
    private List<Behavior> behaviorList = new ArrayList<>();
    private boolean clingingOn;

    public DamselCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }

    @Override
    public void start() {

        BehavedObject damsel = introduceBehavedObject(DamselCharacter.class.getName(),
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius,
                null);

        damsel.behave(
                Arrays.asList(
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                                getContext().updatePhysicalObjectExtra("health", health);
                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                float damage = CollisionModel.calculateDamage(damsel.getObject(), object, impact);

                                if (damage != 0f) {
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
                                if (health <= 0) {
                                    DamselCharacter.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f), initialSpeedMagnitude),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, initialSpeedMagnitude, getContext(), targetList())


                ));
    }


    public Behavior lookingForAHero() {
        return new CruisingBehavior(5f, initialSpeedMagnitude);
    }

    /*
        public Behavior runFrom() {
            return new FlightBehavior(FlightBehavior.FlightStyle.FLY_AWAY, radiusOfAttack /2, initialSpeedMagnitude * 2, getContext());
        }

        public Behavior clingOn() {
            return new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, initialSpeedMagnitude, getContext());
        }
    */
    public void missionComplete() {

    }

    public GameCharacterContext damselContext() {
        return getContext();
    }

    public boolean isClingingOn() {
        return clingingOn;
    }

}

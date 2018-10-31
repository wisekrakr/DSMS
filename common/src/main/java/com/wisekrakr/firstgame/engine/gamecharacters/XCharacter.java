package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.*;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.*;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.*;

public class XCharacter extends AbstractNonPlayerGameCharacter {
    private List<String> targets;
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;

    public XCharacter(List<String> targets, Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.targets = targets;
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }

    @Override
    public void start() {
        BehavedObject middle = introduceBehavedObject(
                AttackingCharacter.class.getName(),
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius, null);

        middle.behave(
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
                                    XCharacter.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f), initialSpeedMagnitude),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack +100f, initialSpeedMagnitude + 30f, getContext(), targets),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack , 0.8f, XCharacter.this.getContext(), targets, new CharacterFactory<AbstractNonPlayerGameCharacter>() {
                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                                return new SplashBulletCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        5f,
                                        3f,
                                        3f,
                                        Visualizations.BOULDER,
                                        getContext()
                                );
                            }
                        })

                ));
    }


}





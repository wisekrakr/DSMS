package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.*;
import com.wisekrakr.firstgame.engine.physicalobjects.*;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.*;

public class XCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;
    private List<PhysicalObject>physicalObjects = new ArrayList<>();
    private PhysicalObject target;

    public XCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }

    @Override
    public void start() {
        BehavedObject middle = introduceBehavedObject("Test Physical Object",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                health,
                0,
                Visualizations.TEST,
                initialRadius);

        float x = getContext().getPhysicalObject().getPosition().x + getContext().getPhysicalObject().getCollisionRadius();
        float y = getContext().getPhysicalObject().getPosition().y + getContext().getPhysicalObject().getCollisionRadius();

        float deltaX = ((float) Math.cos(getContext().getPhysicalObject().getOrientation()));
        float deltaY = ((float) Math.sin(getContext().getPhysicalObject().getOrientation()));



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
                                    XCharacter.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
/*
                                List<NearPhysicalObject>nearPhysicalObjects =
                                        XCharacter.this.getContext().findNearbyPhysicalObjects(getContext().getSubject(), radiusOfAttack);

                                if (!nearPhysicalObjects.isEmpty()){
                                    for (NearPhysicalObject nearPhysicalObject: nearPhysicalObjects){
                                        target = nearPhysicalObject.getObject();
                                    }
                                }
*/

                            }
                        }, new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack * 2, null, getContext())
                        /*
                        , new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack, 1f, getContext(), new CharacterFactory<AbstractNonPlayerGameCharacter>() {
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
                        })
                        */
                        ));
    }

}





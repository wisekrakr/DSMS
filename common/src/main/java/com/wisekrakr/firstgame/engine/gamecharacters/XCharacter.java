package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.*;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.*;

import java.util.*;

public class XCharacter extends AbstractNonPlayerGameCharacter {

    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;

    public XCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack) {

        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;

    }

    @Override
    public void start() {
        BehavedObject middle = introduceBehavedObject(
                "Test boi",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        XCharacter.this.getContext().removeMyself();
                    }
                });

        getContext().tagPhysicalObject(middle.getObject(), Tags.TEST_CHARACTER);
        getContext().tagPhysicalObject(middle.getObject(), Tags.ATTACKER);

        AbstractNPCTools tools = getContext().npcTools(middle.getObject());

        tools.tools().addTargetName(Tags.FRIENDLY);
        tools.tools().addTargetName(Tags.PLAYER);
        tools.tools().addTargetName(Tags.ATTACKER);

        tools.tools().healthIndicator(1000f);

        middle.behave(
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
                                    tools.tools().healthDamage(XCharacter.this.getContext(), object, impact);
                                }
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                tools.tools().weaponry(XCharacter.this.getContext(), CharacterTools.Weaponry.BULLETS, 0.8f, radiusOfAttack);

                                if (tools.tools().getHealth() <= 0){
                                    XCharacter.this.getContext().removeMyself();

                                    getContext().addCharacter(new ExplosionCharacter(
                                            getContext().getSubject().getPosition(),
                                            GameHelper.generateRandomNumberBetween(5f, 20f),
                                            GameHelper.randomDirection(),
                                            5,
                                            getContext().getSubject().getCollisionRadius() * 2,
                                            2f,
                                            Visualizations.EXPLOSION),
                                            null);

                                }
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f),
                                initialSpeedMagnitude
                        ),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW,
                                radiusOfAttack +100f,
                                initialSpeedMagnitude + 30f,
                                getContext(),
                                tools.tools().targetList(getContext())
                        )
                ));
    }


}





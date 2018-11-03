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

public class NPCSpeedyDodger extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;

    public NPCSpeedyDodger(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
    }

    @Override
    public void start() {
        BehavedObject speedy = introduceBehavedObject(AbstractNPCTools.class.getName(),
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.TEST,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        NPCSpeedyDodger.this.getContext().removeMyself();
                    }
                });

        getContext().tagPhysicalObject(speedy.getObject(), Tags.ATTACKER);
        getContext().tagPhysicalObject(speedy.getObject(), Tags.BULLET_ATTACKER);
        getContext().tagPhysicalObject(speedy.getObject(), Tags.DODGER);

        AbstractNPCTools tools = getContext().npcTools(speedy.getObject());

        tools.tools().addTargetName(Tags.ATTACKER);
        tools.tools().addAvoidName(Tags.ATTACKER);

        speedy.behave(
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
                                tools.tools().weaponry(NPCSpeedyDodger.this.getContext(), CharacterTools.Weaponry.BULLETS, 0.5f, radiusOfAttack);
                            }
                        },
                        new CruisingBehavior(GameHelper.generateRandomNumberBetween(5f, 10f),
                                initialSpeedMagnitude
                        ),
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW,
                                radiusOfAttack +100f,
                                initialSpeedMagnitude + 50f,
                                getContext(),
                                tools.tools().targetList(getContext())
                        ),
                        new DefenseBehavior(DefenseBehavior.DefenseStyle.DODGE,
                                radiusOfAttack +30f,
                                initialSpeedMagnitude + 80f,
                                null,
                                getContext()
                        )
                ));
    }


}





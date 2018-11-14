package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.ShieldCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.Tags;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.ArrayList;
import java.util.List;

public class DefenseBehavior extends AbstractBehavior {

    private float radiusOfAttack;
    private DefenseStyle defenseStyle;
    private Float dodgeSpeed;
    private Float minusInHundredsShieldTime;
    private GameCharacterContext characterContext;
    private ShieldCharacter shield;

    public DefenseBehavior(DefenseStyle defenseStyle, float radiusOfAttack, Float dodgeSpeed, Float minusInHundredsShieldTime, GameCharacterContext characterContext) {
        this.radiusOfAttack = radiusOfAttack;
        this.defenseStyle = defenseStyle;
        this.dodgeSpeed = dodgeSpeed;
        this.minusInHundredsShieldTime = minusInHundredsShieldTime;
        this.characterContext = characterContext;
    }

    public enum DefenseStyle {
        DODGE, SHIELDS_UP,
    }

    @Override
    public void elapseTime(float clock, float delta) {

        List<NearPhysicalObject> nearbyPhysicalObjects =
                characterContext.findNearbyPhysicalObjects(getContext().getSubject(), radiusOfAttack);

        if (!nearbyPhysicalObjects.isEmpty()) {
            for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {
                PhysicalObject target = nearPhysicalObject.getObject();

                float angle = GameHelper.angleBetween(getContext().getSubject().getPosition(), target.getPosition());

                if (!target.getTags().contains(Tags.DEBRIS) && target != getContext().getSubject()) {

                    if (GameHelper.distanceBetweenPhysicals(getContext().getSubject(), target) < radiusOfAttack) {

                        switch (defenseStyle) {
                            case DODGE:
                                //move away from what is coming, fast.

                                getContext().updatePhysicalObject(
                                        null,
                                        null,
                                        angle,
                                        dodgeSpeed,
                                        -angle,
                                        null,
                                        null,
                                        null
                                );

                                //System.out.println(getContext().getSubject().getName() + StringHelper.ANSI_BLACK_BACKGROUND + StringHelper.ANSI_WHITE + " dodging target: " + StringHelper.ANSI_RESET + target.getName());

                                break;

                            case SHIELDS_UP:
                                //creates a shield around the character

                                List<ShieldCharacter>shields = new ArrayList<>();

                                if (shields.isEmpty()) {
                                    shield = new ShieldCharacter(getContext().getSubject().getPosition(),
                                            getContext().getSubject().getSpeedMagnitude(),
                                            getContext().getSubject().getSpeedDirection(),
                                            getContext().getSubject().getCollisionRadius() + 5f,
                                            100f,
                                            minusInHundredsShieldTime,
                                            Visualizations.SHIELD,
                                            characterContext);

                                    characterContext.addCharacter(shield, null);  // TODO: implement listener
                                    shields.add(shield);
                                }else {
                                    shields.clear();
                                }

                                break;
                            default:
                                System.out.println("No Defense Behavior chosen for : " + getContext().getSubject().getName());

                        }


                    }
                }
            }
        }
    }
}

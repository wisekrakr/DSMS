package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.StringHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.Tags;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.List;
import java.util.Set;

public class AttackBehavior extends AbstractBehavior {

    private float radiusOfAttack;
    private float fireRate;
    private Set<String> targetList;
    private AttackStyle attackStyle;
    private GameCharacterContext master;
    private Float lastShot;
    private CharacterFactory<?>factory;

    //TODO: delete all this bullllshjiiit

    public AttackBehavior(AttackStyle attackStyle, float radiusOfAttack, float fireRate, GameCharacterContext master, Set<String> targetList, CharacterFactory<?> factory) {
        this.radiusOfAttack = radiusOfAttack;
        this.fireRate = fireRate;
        this.targetList = targetList;
        this.attackStyle = attackStyle;
        this.master = master;
        this.factory = factory;
    }

    public enum AttackStyle {
        SHOOT, BUMP,
    }

    @Override
    public void elapseTime(float clock, float delta) {

        List<NearPhysicalObject> nearbyPhysicalObjects =
                master.findNearbyPhysicalObjects(getContext().getSubject(), radiusOfAttack);
        if (!nearbyPhysicalObjects.isEmpty()) {
            for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {

                PhysicalObject target = nearPhysicalObject.getObject();

                float angle = GameHelper.angleBetween(getContext().getSubject().getPosition(), target.getPosition());

                float x = getContext().getSubject().getPosition().x;
                float y = getContext().getSubject().getPosition().y;

                float deltaX = ((float) Math.cos(getContext().getSubject().getOrientation()));
                float deltaY = ((float) Math.sin(getContext().getSubject().getOrientation()));

                if (!target.getTags().contains(Tags.PROJECTILE) && !target.getTags().contains(Tags.DEBRIS) && target != getContext().getSubject()) {

                    for (String string: targetList){
                        if (target.getTags().contains(string)) {

                            if (GameHelper.distanceBetweenPhysicals(getContext().getSubject(), target) < radiusOfAttack) {

                                switch (attackStyle) {
                                    case SHOOT:
                                        //angle towards target and create new weapon character

                                        getContext().updatePhysicalObject(
                                                null,
                                                null,
                                                angle,
                                                null,
                                                angle + getContext().getSubject().getCollisionRadius() * 2,
                                                null,
                                                null
                                        );

                                        if (lastShot == null) {
                                            lastShot = clock;
                                        }

                                        if (fireRate != 0) {
                                            if (clock - lastShot > fireRate) {

                                                GameCharacter newObject = factory.createCharacter(new Vector2(x + (getContext().getSubject().getCollisionRadius() +
                                                                master.getPhysicalObject().getCollisionRadius() / 5) * deltaX,
                                                                y + (getContext().getSubject().getCollisionRadius() +
                                                                        master.getPhysicalObject().getCollisionRadius() / 5) * deltaY),
                                                        getContext().getSubject().getSpeedMagnitude() + 200f,
                                                        getContext().getSubject().getOrientation(),
                                                        getContext().getSubject().getSpeedDirection(),
                                                        master.getPhysicalObject().getCollisionRadius() / 5,
                                                        radiusOfAttack
                                                );

                                                getContext().addCharacter(newObject, null);
                                                //System.out.println(getContext().getSubject().getName() + StringHelper.ANSI_RED_BACKGROUND + " shooting target: " + StringHelper.ANSI_RESET + target.getName());
                                                lastShot = clock;
                                            }
                                        }
                                        break;

                                    case BUMP:
                                        //angle towards target and when colliding, impact with significance


                                        break;

                                    default:
                                        System.out.println("No Attacking Behavior chosen for : " + getContext().getSubject().getName());

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

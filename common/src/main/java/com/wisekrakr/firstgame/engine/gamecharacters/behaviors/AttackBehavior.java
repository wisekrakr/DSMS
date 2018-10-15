package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.List;

public class AttackBehavior extends AbstractBehavior {

    private float radiusOfAttack;
    private float fireRate;
    private float damage;
    private AttackStyle attackStyle;
    private GameCharacterContext master;
    private Float lastShot;
    private CharacterFactory<?>factory;

    public AttackBehavior(AttackStyle attackStyle, float radiusOfAttack, float fireRate, GameCharacterContext master, CharacterFactory<?> factory) {
        this.radiusOfAttack = radiusOfAttack;
        this.fireRate = fireRate;
        this.damage = damage;
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

                String name = target.getName();

                float x = getContext().getSubject().getPosition().x;
                float y = getContext().getSubject().getPosition().y;

                float deltaX = ((float) Math.cos(getContext().getSubject().getOrientation()));
                float deltaY = ((float) Math.sin(getContext().getSubject().getOrientation()));


                if (!name.contains("weapon") && !name.contains("debris") && target != getContext().getSubject() &&
                        !name.contains(getContext().getSubject().getName()) && target != master.getPhysicalObject()) {

                    if (GameHelper.distanceBetweenPhysicals(getContext().getSubject(), target) < radiusOfAttack) {

                        switch (attackStyle) {
                            case SHOOT:
                                //angle towards target and create new bullet character

                                getContext().updatePhysicalObject(
                                        null,
                                        null,
                                        angle,
                                        null,
                                        angle,
                                        null,
                                        null,
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
                                                200f,
                                                getContext().getSubject().getOrientation(),
                                                getContext().getSubject().getSpeedDirection(),
                                                master.getPhysicalObject().getCollisionRadius() / 5,
                                                radiusOfAttack,
                                                0,
                                                damage);

                                        getContext().addCharacter(newObject);
                                        System.out.println(getContext().getSubject().getName() + " = shooting at target: " + target.getName());
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

package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.MiscBehaviors;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class HomingMissileCharacter extends AbstractNonPlayerGameCharacter {

    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private float missileAge;
    private float damage;
    private float radius;
    private float radiusOfAttack;
    private final Visualizations visualizations;
    private GameCharacterContext master;

    public HomingMissileCharacter(Vector2 position, float speedMagnitude, float speedDirection, float missileAge, float damage, float radius, float radiusOfAttack, Visualizations visualizations, GameCharacterContext master) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.missileAge = missileAge;
        this.damage = damage;
        this.radius = radius;
        this.radiusOfAttack = radiusOfAttack;
        this.visualizations = visualizations;
        this.master = master;
    }


    @Override
    public void start() {
        BehavedObject missile = introduceBehavedObject("weapon",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                0,
                damage,
                visualizations,
                radius);

        missile.behave(
                Arrays.asList(

                        new AbstractBehavior(){
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", radius);
                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {

                                if (!object.getName().contains("debris") && object != master.getPhysicalObject()) {
                                    getContext().updatePhysicalObject(null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            object.getHealth() - getContext().getSubject().getDamage(),
                                            null,
                                            null,
                                            null
                                    );
                                    getContext().removePhysicalObject();
                                    HomingMissileCharacter.this.getContext().removeMyself();
                                }
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {
                                //super.elapseTime(clock, delta);
                                missileAge = missileAge - delta;
                                if (missileAge < 0) {
                                    MiscBehaviors.exploding(HomingMissileCharacter.this.getContext(), 5, getContext().getSubject().getCollisionRadius() * 3, 5f);

                                    getContext().removePhysicalObject();
                                    HomingMissileCharacter.this.getContext().removeMyself();
                                }
                            }
                        },
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, speedMagnitude + 20f, master)

        ));
    }

}





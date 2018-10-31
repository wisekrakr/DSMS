package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;
import java.util.List;

public class HomingMissileCharacter extends AttackingCharacter {

    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private float missileAge;
    private float damage;
    private float radius;
    private float radiusOfAttack;
    private final Visualizations visualizations;
    private GameCharacterContext master;
    private List<String> mastersTargetList;


    public HomingMissileCharacter(Vector2 position, float speedMagnitude, float speedDirection, float missileAge, float damage, float radius, float radiusOfAttack, Visualizations visualizations, GameCharacterContext master, List<String> mastersTargetList) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.missileAge = missileAge;
        this.damage = damage;
        this.radius = radius;
        this.radiusOfAttack = radiusOfAttack;
        this.visualizations = visualizations;
        this.master = master;
        this.mastersTargetList = mastersTargetList;
    }

    @Override
    public void start() {
        BehavedObject missile = introduceBehavedObject("homing missile",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                visualizations,
                radius, null);

        getContext().tagPhysicalObject(missile.getObject(), Tags.PROJECTILE);

        missile.behave(
                Arrays.asList(
                        new AbstractBehavior() {
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", radius);
                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                if (master.getPhysicalObject() != object) {
                                    float damage = CollisionModel.calculateDamage(missile.getObject(), object, impact);

                                    if (damage != 0f) {
                                        getContext().updatePhysicalObject(null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null,
                                                null
                                        );

                                        getContext().addCharacter(new ExplosionCharacter(getContext().getSubject().getPosition(),
                                                GameHelper.generateRandomNumberBetween(5f, 20f),
                                                GameHelper.randomDirection(),
                                                10,
                                                radius * 3,
                                                5f,
                                                Visualizations.EXPLOSION), null);
                                        getContext().removePhysicalObject();
                                        HomingMissileCharacter.this.getContext().removeMyself();

                                    }
                                }
                            }

                            @Override
                            public void elapseTime(float clock, float delta) {

                                missileAge = missileAge - delta;
                                if (missileAge < 0) {
                                    getContext().addCharacter(new ExplosionCharacter(getContext().getSubject().getPosition(),
                                            GameHelper.generateRandomNumberBetween(5f, 20f),
                                            GameHelper.randomDirection(),
                                            10,
                                            radius,
                                            5f,
                                            Visualizations.EXPLOSION), null);

                                    getContext().removePhysicalObject();
                                    HomingMissileCharacter.this.getContext().removeMyself();
                                }
                                System.out.println(mastersTargetList.get(0));
                            }

                        },
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, speedMagnitude + 20f, master, mastersTargetList)

                ));
    }

}





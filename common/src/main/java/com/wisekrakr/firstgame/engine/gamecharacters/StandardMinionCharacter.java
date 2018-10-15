package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.Arrays;

public class StandardMinionCharacter extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;
    private float damage;
    private Visualizations visualizations;
    private AttackBehavior.AttackStyle attackStyle;
    private GameCharacterContext master;

    public StandardMinionCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health, float damage, Visualizations visualizations, AttackBehavior.AttackStyle attackStyle, GameCharacterContext master) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
        this.damage = damage;
        this.visualizations = visualizations;
        this.attackStyle = attackStyle;
        this.master = master;
    }


    @Override
    public void start() {
        BehavedObject attackerA = introduceBehavedObject("minion",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                health,
                damage,
                visualizations,
                initialRadius);

        attackerA.behave(
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
                                    StandardMinionCharacter.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new CruisingBehavior(3f, initialSpeedMagnitude)
                        //new AttackBehavior(attackStyle, radiusOfAttack, 1f, damage, master, )
                ));
    }


}





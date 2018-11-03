package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.Behavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors.CruisingBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.List;

public class PervertCharacter extends AbstractNonPlayerGameCharacter {

    private Vector2 initialPosition;
    private float initialRadius;
    private float initialDirection;
    private float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;
    private Behavior desiredBehavior;
    private List<Behavior> behaviorList;

    public PervertCharacter(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
    }

    @Override
    public void start() {
        BehavedObject pervert = introduceBehavedObject(
                "dirty rotten pervert",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                Visualizations.COCKPIT,
                initialRadius, null);

        getContext().tagPhysicalObject(pervert.getObject(), Tags.FOLLOWER);
        getContext().tagPhysicalObject(pervert.getObject(), Tags.ATTACKER);

        //pervert.behave(Arrays.asList(lookingForADamsel(),chaseAfter(), circlingDamsel()));
    }

    private List<Behavior> scenarioBehaviorList() {
        return behaviorList;
    }

    public void addToBehaviorList(Behavior behavior){
        behaviorList.add(behavior);

    }

    public Behavior lookingForADamsel(){
        return desiredBehavior = new CruisingBehavior(10f, initialSpeedMagnitude){
            @Override
            public void start() {
                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                getContext().updatePhysicalObjectExtra("health", health);
            }

            @Override
            public void elapseTime(float clock, float delta) {
                if (health < 0){
                    PervertCharacter.this.getContext().removeMyself();
                }
            }
        };
    }
/*
    public Behavior chaseAfter() {
        return desiredBehavior = new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, initialSpeedMagnitude, getContext());
    }

    public Behavior circlingDamsel(){
        return desiredBehavior = new FlightBehavior(FlightBehavior.FlightStyle.CIRCLING, radiusOfAttack/3, initialSpeedMagnitude, getContext());
    }
*/
    public GameCharacterContext getPervertContext(){
        return getContext();
    }

}

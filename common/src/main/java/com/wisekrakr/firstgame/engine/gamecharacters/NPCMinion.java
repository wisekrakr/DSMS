package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.Arrays;
import java.util.Set;

public class NPCMinion extends AbstractNonPlayerGameCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private Visualizations visualizations;
    private Set<String> targetList;
    private final GameCharacterContext master;

    public NPCMinion(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, Visualizations visualizations, Set<String> targetList, GameCharacterContext master) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.visualizations = visualizations;
        this.targetList = targetList;
        this.master = master;
    }

    @Override
    public void start() {
        BehavedObject minionMain = introduceBehavedObject(
                "minion",
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                visualizations,
                initialRadius,
                new BehavedObjectListener() {
                    @Override
                    public void removed() {
                        NPCMinion.this.getContext().removeMyself();
                    }
                });

        getContext().tagPhysicalObject(minionMain.getObject(), Tags.MINION);
        getContext().tagPhysicalObject(minionMain.getObject(), Tags.BULLET_ATTACKER);

        //TODO: return home  function when nothing to shoot is nearby

        minionMain.behave(
                Arrays.asList(
                        new AbstractBehavior(){
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);

                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                if (!object.getTags().contains(Tags.DEBRIS) && object != master.getPhysicalObject() && !object.getTags().contains(Tags.MINION)) {
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

                        },
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, initialSpeedMagnitude, getContext(), targetList),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack/2, 1.5f, getContext(), targetList, new CharacterFactory<AbstractNonPlayerGameCharacter>() {

                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack) {
                                return new BulletCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        3f,
                                        radius,
                                        Visualizations.LEFT_CANNON,
                                        getContext());
                            }
                        })
                ));
    }


}





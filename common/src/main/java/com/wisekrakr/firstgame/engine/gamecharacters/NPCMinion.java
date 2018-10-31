package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AttackBehavior;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.FlightBehavior;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.Arrays;
import java.util.List;

public class NPCMinion extends AttackingCharacter {
    private Vector2 initialPosition;
    private float initialRadius;
    private final float initialDirection;
    private final float initialSpeedMagnitude;
    private float radiusOfAttack;
    private float health;
    private Visualizations visualizations;
    private List<String> targetList;
    private final GameCharacterContext master;

    public NPCMinion(Vector2 initialPosition, float initialRadius, float initialDirection, float initialSpeedMagnitude, float radiusOfAttack, float health, Visualizations visualizations, List<String> targetList, GameCharacterContext master) {
        this.initialPosition = initialPosition;
        this.initialRadius = initialRadius;
        this.initialDirection = initialDirection;
        this.initialSpeedMagnitude = initialSpeedMagnitude;
        this.radiusOfAttack = radiusOfAttack;
        this.health = health;
        this.visualizations = visualizations;
        this.targetList = targetList;
        this.master = master;
    }

    @Override
    public void start() {
        BehavedObject minionMain = introduceBehavedObject(NPCMinion.class.getName(),
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                visualizations,
                initialRadius, null);

        //TODO: return home  function when nothing to shoot is nearby

        minionMain.behave(
                Arrays.asList(
                        new AbstractBehavior(){
                            @Override
                            public void start() {
                                getContext().updatePhysicalObjectExtra("radius", initialRadius);
                                getContext().updatePhysicalObjectExtra("health", health);

                            }

                            @Override
                            public void collide(PhysicalObject object, Vector2 epicentre, float impact) {
                                if (!object.getTags().contains(Tags.DEBRIS) && object != master.getPhysicalObject() && !object.getName().contains(NPCMinion.class.getName())) {
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
                                if (health <= 0){
                                    NPCMinion.this.getContext().removeMyself();
                                    getContext().removePhysicalObject();
                                }
                            }
                        },
                        new FlightBehavior(FlightBehavior.FlightStyle.FOLLOW, radiusOfAttack, initialSpeedMagnitude, getContext(), targetList),
                        new AttackBehavior(AttackBehavior.AttackStyle.SHOOT, radiusOfAttack/2, 1.5f, getContext(), targetList, new CharacterFactory<AbstractNonPlayerGameCharacter>() {

                            @Override
                            public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                                return new BulletCharacter(position,
                                        speedMagnitude,
                                        orientation,
                                        3f,
                                        radius,
                                        getContext().getPhysicalObject().getCollisionRadius(),
                                        Visualizations.LEFT_CANNON,
                                        getContext());
                            }
                        })
                ));
    }


}





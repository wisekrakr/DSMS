package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.StandardMinionCharacter;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.ArrayList;
import java.util.List;

public class DuplicationBehavior extends AbstractBehavior {

    private float radiusOfAttack;
    private Integer number;
    private Float spawnInterval;
    private GameCharacterContext master;
    private DuplicationStyle duplicationStyle;
    private Float lastShot;
    private ArrayList<GameCharacter>characters = new ArrayList<>();

    public DuplicationBehavior(DuplicationStyle duplicationStyle, float radiusOfAttack, Integer number, Float spawnInterval, GameCharacterContext master) {
        this.duplicationStyle = duplicationStyle;
        this.radiusOfAttack = radiusOfAttack;
        this.number = number;
        this.spawnInterval = spawnInterval;
        this.master = master;
    }

    public enum DuplicationStyle {
        DEPLOY_MINIONS, MULTIPLY,
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


                if (!name.contains("weapon") && !name.contains("debris") && target != getContext().getSubject() &&
                        !name.contains(getContext().getSubject().getName()) && target != master.getPhysicalObject()) {

                    if (GameHelper.distanceBetweenPhysicals(getContext().getSubject(), target) < radiusOfAttack) {

                        switch (duplicationStyle){
                            case DEPLOY_MINIONS:
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

                                if (spawnInterval != null) {
                                    if (clock - lastShot > spawnInterval) {
                                        if (characters.size() < number){
                                            GameCharacter newObject = new StandardMinionCharacter(new Vector2(x + getContext().getSubject().getCollisionRadius(),
                                                            y + getContext().getSubject().getCollisionRadius()),
                                                    GameHelper.generateRandomNumberBetween(20f, 60f),
                                                    angle,
                                                    GameHelper.generateRandomNumberBetween(10f, 20f),
                                                    radiusOfAttack,
                                                    GameHelper.generateRandomNumberBetween(20f, 60f),
                                                    GameHelper.generateRandomNumberBetween(5f, 10f),
                                                    Visualizations.COCKPIT,
                                                    AttackBehavior.AttackStyle.SHOOT,
                                                    master);

                                            getContext().addCharacter(newObject);
                                            characters.add(newObject);

                                            System.out.println(getContext().getSubject().getName() + " = deploying minions at target: " + target.getName());
                                        }
                                        lastShot = clock;
                                    }
                                }

                                break;

                            case MULTIPLY:
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

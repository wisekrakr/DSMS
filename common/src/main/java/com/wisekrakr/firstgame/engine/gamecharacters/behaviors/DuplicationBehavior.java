package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.Tags;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DuplicationBehavior extends AbstractBehavior {

    private float radiusOfAttack;
    private Integer number;
    private Float spawnInterval;
    private GameCharacterContext master;
    private Set<String> targetList;
    private DuplicationStyle duplicationStyle;
    private Float lastShot;
    private ArrayList<GameCharacter>characters = new ArrayList<>();
    private CharacterFactory factory;

    public DuplicationBehavior(DuplicationStyle duplicationStyle, float radiusOfAttack, Integer number, Float spawnInterval, GameCharacterContext master, Set<String> targetList, CharacterFactory factory) {
        this.duplicationStyle = duplicationStyle;
        this.radiusOfAttack = radiusOfAttack;
        this.number = number;
        this.spawnInterval = spawnInterval;
        this.master = master;
        this.targetList = targetList;
        this.factory = factory;
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


                float x = getContext().getSubject().getPosition().x;
                float y = getContext().getSubject().getPosition().y;

                if (!target.getTags().contains(Tags.PROJECTILE) && !target.getTags().contains(Tags.DEBRIS) && target != getContext().getSubject()) {

                    for (String string: targetList){
                        if (target.getTags().contains(string)) {

                            if (GameHelper.distanceBetweenPhysicals(getContext().getSubject(), target) < radiusOfAttack) {

                                switch (duplicationStyle) {
                                    case DEPLOY_MINIONS:
                                        //deploy character of your choosing as a minion

                                        if (lastShot == null) {
                                            lastShot = clock;
                                        }

                                        if (spawnInterval != null) {
                                            if (clock - lastShot > spawnInterval) {
                                                if (characters.size() < number) {
                                                    GameCharacter newObject = factory.createCharacter(new Vector2(x + getContext().getSubject().getCollisionRadius()*2,
                                                                    y + getContext().getSubject().getCollisionRadius()*2),
                                                            GameHelper.generateRandomNumberBetween(100, 150f),
                                                            angle,
                                                            angle,
                                                            GameHelper.generateRandomNumberBetween(getContext().getSubject().getCollisionRadius() / 4, getContext().getSubject().getCollisionRadius() / 2),
                                                            radiusOfAttack
                                                    );

                                                    getContext().addCharacter(newObject, null); // TODO: implement listener
                                                    characters.add(newObject);

                                                    //System.out.println(getContext().getSubject().getName() + StringHelper.ANSI_PURPLE_BACKGROUND + StringHelper.ANSI_WHITE + " deploying minions at: " + StringHelper.ANSI_RESET + target.getName());
                                                }
                                                lastShot = clock;
                                            }
                                        }

                                        break;

                                    case MULTIPLY:

                                        break;

                                    default:
                                        System.out.println("No Duplication Behavior chosen for : " + getContext().getSubject().getName());

                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

package com.wisekrakr.firstgame.engine.gamecharacters.behaviors.subbehaviors;


import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.behaviors.AbstractBehavior;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;


public class DeployMinionsBehavior extends AbstractBehavior {
    private Float lastCreation;
    private GameCharacterContext context;
    private CharacterFactory factory;
    private float spawnInterval;
    private float radiusOfAttack;

    public DeployMinionsBehavior(GameCharacterContext context, CharacterFactory<?>factory, float spawnInterval, float radiusOfAttack) {
        this.context = context;
        this.factory = factory;
        this.spawnInterval = spawnInterval;
        this.radiusOfAttack = radiusOfAttack;
    }



    @Override
    public void elapseTime(float clock, float delta) {
        if (lastCreation == null) {
            lastCreation = clock;
        }

        if ((clock - lastCreation) > spawnInterval) {

            context.addCharacter(factory.createCharacter(new Vector2(context.getPhysicalObject().getPosition().x + context.getPhysicalObject().getCollisionRadius(),
                            context.getPhysicalObject().getPosition().y + context.getPhysicalObject().getCollisionRadius()),
                    GameHelper.generateRandomNumberBetween(20f, 60f),
                    GameHelper.randomDirection(),
                    GameHelper.randomDirection(),
                    GameHelper.generateRandomNumberBetween(10f, 20f),
                    radiusOfAttack
            ), null); // TODO: implement listener
            lastCreation = clock;
        }


    }


}

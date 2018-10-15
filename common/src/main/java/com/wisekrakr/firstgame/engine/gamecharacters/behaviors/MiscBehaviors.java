package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.BulletCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.ExplosionCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.ArrayList;
import java.util.List;

public class MiscBehaviors extends AbstractBehavior {

    private static Float lastShot;
    private static Float lastCreation;

    public static void normalize(GameCharacterContext context, Vector2 initialPosition, Float initialRadius, Float initialDirection, Float initialSpeedMagnitude, Float health, Float damage){
        context.updatePhysicalObject(
                context.getPhysicalObject(),
                null,
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                health,
                damage,
                null,
                initialRadius);
    }

    public static void deployMinions(GameCharacterContext context, PhysicalObject target, int numberOfMinions, CharacterFactory factory, float spawnInterval, float radiusOfAttack, float clock){

        if (lastCreation == null) {
            lastCreation = clock;
        }

        List<GameCharacter> list = new ArrayList<>();

        if (target != null) {
            if (list.size() < numberOfMinions ) {
                if ((clock - lastCreation) > spawnInterval) {

                    GameCharacter newObject = factory.createCharacter(new Vector2(context.getPhysicalObject().getPosition().x + context.getPhysicalObject().getCollisionRadius(),
                                    context.getPhysicalObject().getPosition().y + context.getPhysicalObject().getCollisionRadius()),
                            GameHelper.generateRandomNumberBetween(20f, 60f),
                            GameHelper.randomDirection(),
                            GameHelper.randomDirection(),
                            GameHelper.generateRandomNumberBetween(10f, 20f),
                            radiusOfAttack,
                            GameHelper.generateRandomNumberBetween(20f, 60f),
                            GameHelper.generateRandomNumberBetween(5f, 10f));

                    context.addCharacter(newObject);
                    list.add(newObject);
                    System.out.println(list.size());

                    lastCreation = clock;
                }
            }
        }
    }

    public static void rotating(GameCharacterContext context, float rotateSpeed, float delta){
        float rotatingAngle = context.getPhysicalObject().getOrientation();
        rotatingAngle += rotateSpeed * delta;
        context.updatePhysicalObject(context.getPhysicalObject(),
                null,
                null,
                rotatingAngle,
                null,
                null,
                null,
                null,
                null,
                null);
        System.out.println(context.getPhysicalObject().getName() + " = rotating");
    }

    public static void exploding(GameCharacterContext context, int debrisParts, float debrisMass, float debrisAge){
        context.addCharacter(new ExplosionCharacter(context.getPhysicalObject().getPosition(),
                GameHelper.generateRandomNumberBetween(5f, 20f),
                GameHelper.randomDirection(),
                debrisParts,
                debrisMass,
                debrisAge,
                Visualizations.EXPLOSION));
    }

}

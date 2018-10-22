package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.ExplosionCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.physicalobjects.NearPhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;

import java.util.ArrayList;
import java.util.List;

public class MiscBehaviors extends AbstractBehavior {

    private static Float lastShot;
    private static Float lastCreation;

    public static void normalize(BehaviorContext context, Vector2 initialPosition, Float initialRadius, Float initialDirection, Float initialSpeedMagnitude, Float health, Float damage){
        context.updatePhysicalObject(
                null,
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                health,
                damage,
                null,
                initialRadius
        );
    }

    public static PhysicalObject getTarget(GameCharacterContext context, float radiusOfAttack){
        List<NearPhysicalObject> nearbyPhysicalObjects =
                context.findNearbyPhysicalObjects(context.getPhysicalObject(), radiusOfAttack);
        PhysicalObject target = null;

        if (!nearbyPhysicalObjects.isEmpty()) {
            for (NearPhysicalObject nearPhysicalObject : nearbyPhysicalObjects) {
                target = nearPhysicalObject.getObject();
            }
        }
        return target;
    }

    public static void deployMinions(BehaviorContext context, PhysicalObject target, int numberOfMinions, CharacterFactory factory, float spawnInterval, float radiusOfAttack, float clock){

        if (lastCreation == null) {
            lastCreation = clock;
        }

        List<GameCharacter> list = new ArrayList<>();

        if (target != null) {
            if (list.size() < numberOfMinions ) {
                if ((clock - lastCreation) > spawnInterval) {

                    GameCharacter newObject = factory.createCharacter(new Vector2(context.getSubject().getPosition().x + context.getSubject().getCollisionRadius(),
                                    context.getSubject().getPosition().y + context.getSubject().getCollisionRadius()),
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

    public static void rotating(BehaviorContext context, float rotateSpeed, float delta){
        float rotatingAngle = context.getSubject().getOrientation();
        rotatingAngle += rotateSpeed * delta;
        context.updatePhysicalObject(null,
                null,
                null,
                rotatingAngle,
                null,
                null,
                null,
                null,
                null
                );
        System.out.println(context.getSubject().getName() + " = rotating");
    }

    public static void exploding(BehaviorContext context, int debrisParts, float debrisMass, float debrisAge){
        context.addCharacter(new ExplosionCharacter(context.getSubject().getPosition(),
                GameHelper.generateRandomNumberBetween(5f, 20f),
                GameHelper.randomDirection(),
                debrisParts,
                debrisMass,
                debrisAge,
                Visualizations.EXPLOSION));
    }

    public static void follow(BehaviorContext context, float angle, float speedIncrease){
        context.updatePhysicalObject(
                null,
                null,
                angle,
                speedIncrease,
                angle,
                null,
                null,
                null,
                null
        );
    }
}

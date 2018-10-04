package com.wisekrakr.firstgame.engine.gamecharacters.behaviors;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.gamecharacters.AsteroidCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.BulletCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.GameCharacterContext;
import com.wisekrakr.firstgame.engine.gamecharacters.HomingMissileCharacter;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObjectListener;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;
import com.wisekrakr.firstgame.engine.scenarios.PhysicalObjectFactory;

public class MiscAttackBehaviors {

    private static Float lastShot;

    public static void normalize(GameCharacterContext context, Vector2 initialPosition, Float initialRadius, Float initialDirection, Float initialSpeedMagnitude){
        context.updatePhysicalObject(
                context.getPhysicalObject(),
                null,
                initialPosition,
                initialDirection,
                initialSpeedMagnitude,
                initialDirection,
                null,
                initialRadius);
    }

    public static void chase(GameCharacterContext context, PhysicalObject target, float speedMagnitude){

        if (target != null) {
            float angle = GameHelper.angleBetween(context.getPhysicalObject().getPosition(), target.getPosition());

            context.updatePhysicalObject(
                    context.getPhysicalObject(),
                    null,
                    null,
                    angle,
                    speedMagnitude,
                    angle,
                    null,
                    null);
            System.out.println("target: " + target.getName());
        }
    }

    public static void chasingAndShooting(GameCharacterContext context, PhysicalObject target, Float fireRate, float clock){

        if (target != null && target != context) {

            float angle = GameHelper.angleBetween(context.getPhysicalObject().getPosition(), target.getPosition());

            context.updatePhysicalObject(
                    context.getPhysicalObject(),
                    null,
                    null,
                    angle,
                    null,
                    angle,
                    null,
                    null);

            float x = context.getPhysicalObject().getPosition().x;
            float y = context.getPhysicalObject().getPosition().y;

            float deltaX = ((float) Math.cos(context.getPhysicalObject().getOrientation()));
            float deltaY = ((float) Math.sin(context.getPhysicalObject().getOrientation()));

            if (lastShot == null) {
                lastShot = clock;
            }

            if (fireRate != null) {
                if (clock - lastShot > fireRate) {
                    context.addCharacter(new BulletCharacter(new Vector2(x + context.getPhysicalObject().getCollisionRadius() * deltaX,
                            y + context.getPhysicalObject().getCollisionRadius() * deltaY),
                            200f,
                            context.getPhysicalObject().getOrientation(),
                            3f,
                            20,
                            2f,
                            Visualizations.LEFT_CANNON
                    ));
                    lastShot = clock;
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
                null);
        System.out.println(context.getPhysicalObject().getName() + " = rotating");
    }
}

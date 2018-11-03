package com.wisekrakr.firstgame.engine.gamecharacters;

import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public class CollisionModel {
    public static float calculateDamage(PhysicalObject subject, PhysicalObject object) {

        if (!object.getTags().contains(Tags.DEBRIS)) {

            float force = (float) (0.5f * subject.getCollisionRadius() * Math.sqrt(subject.getSpeedMagnitude()));
            float counterForce = (float) (0.5f * object.getCollisionRadius() * Math.sqrt(subject.getSpeedMagnitude()));


            return Math.abs(force / counterForce); //damage
        }

        return 0f;
    }
}

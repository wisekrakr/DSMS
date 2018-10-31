package com.wisekrakr.firstgame.engine.gamecharacters;

import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;

public class CollisionModel {
    public static float calculateDamage(PhysicalObject subject, PhysicalObject object, float impact) {
        if (!object.getTags().contains(Tags.DEBRIS)) {

            return impact;
        }

        return 0f;
    }
}

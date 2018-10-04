package com.wisekrakr.firstgame.engine.gamecharacters;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameHelper;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.ArrayList;
import java.util.List;

public class BulletCharacter extends AbstractGameCharacter {

    private final Vector2 position;
    private final float speedMagnitude;
    private final float speedDirection;
    private float bulletAge;
    private int ammoCount;
    private float radius;
    private Visualizations visualizations;

    public BulletCharacter(Vector2 position, float speedMagnitude, float speedDirection, float bulletAge, int ammoCount, float radius, Visualizations visualizations) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.bulletAge = bulletAge;
        this.ammoCount = ammoCount;
        this.radius = radius;
        this.visualizations = visualizations;
    }

    @Override
    public void start() {

        for (int i = 0; i < ammoCount; i++) {
            PhysicalObject bit = getContext().addPhysicalObject("weapon",
                    position,
                    speedDirection,
                    speedMagnitude,
                    speedDirection,
                    visualizations,
                    radius,
                    null);

            getContext().updatePhysicalObjectExtra(bit, "radius", radius);
        }
    }

    @Override
    public void elapseTime(float delta) {
        bulletAge = bulletAge - delta;
        if (bulletAge < 0) {
            getContext().removeMyself();
        }
    }


}

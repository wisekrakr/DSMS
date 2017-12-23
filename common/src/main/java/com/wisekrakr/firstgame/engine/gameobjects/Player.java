package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

public class Player extends Spaceship {
    private static final float DEFAULT_PLAYER_SPEED = 40;
    private static final float DEFAULT_PLAYER_ROTATE_SPEED = 2;

    public float rotationRadians;

    public Player(String name, Vector2 position, SpaceEngine space) {
        super(name, position, space);

    }


    public void movement(float deltaTime) {
        Vector2 newPosition = getPosition();
        Vector2 direction = new Vector2(newPosition.x - getPosition().x, newPosition.y - getPosition().y);

        /*
        float playerX = getPosition().x;
        float angle = direction.angleRad();

        Vector2 prevPosition = new Vector2();
        Vector2 rotationVector = new Vector2(getPosition().x - prevPosition.x, getPosition().y - prevPosition.y);
        rotationRadians = rotationVector.angle();
        rotationDegrees = rotationRadians * MathUtils.radiansToDegrees;
        */
    }
}

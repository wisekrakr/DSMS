package com.wisekrakr.firstgame.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.Constants;
import com.wisekrakr.firstgame.Movement.Enums;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by David on 11/15/2017.
 */
public class Asteroid extends BaseObject {

    private static final float ASTEROID_SPEED = 30f;

    Enums.Direction direction;

    public Asteroid(String name, Vector2 position, Vector2 velocity) {
        super(name, position, velocity);
        init();
    }

    @Override
    public float collisionDetector(BaseObject target) {
        return 0;
    }


    @Override
    public void attack(float delta) {


    }

    @Override
    public void init() {
        System.out.println("Asteroid Draw = " + getName());
        int number = 500;
        setPosition(new Vector2(random.nextInt(number), random.nextInt(number)));
        direction = Enums.Direction.DOWN;
    }

    @Override
    public void movement(float delta) {
        Vector2 newPosition = getPosition();

        switch (direction){
            case LEFT:
                newPosition.x -= random.nextInt((int) ASTEROID_SPEED) * Constants.DELTA;
                break;
            case RIGHT:
                newPosition.x += random.nextInt((int) ASTEROID_SPEED) * Constants.DELTA;
                break;
            case UP:
                newPosition.y += random.nextInt((int) ASTEROID_SPEED) * Constants.DELTA;
                break;
            case DOWN:
                newPosition.y -= random.nextInt((int) ASTEROID_SPEED) * Constants.DELTA;
                break;
        }


    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(ShapeRenderer renderer) {

    }
}

package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

public abstract class Spaceship extends GameObject {
    private ThrottleState throttle = ThrottleState.STATUSQUO;
    private SteeringState steering = SteeringState.CENTER;
    private float speed = 0;
    private float angle = (float) Math.PI / 2;


    public Spaceship(String name, Vector2 position, SpaceEngine space) {
        super(name, position, space);

        setCollisionRadius(10);
    }

    public void resetControl() {
        speed = 0;
        angle = (float) Math.PI / 2;
    }

    public enum ThrottleState {
        REVERSE, STATUSQUO, FORWARDS
    }

    public enum SteeringState {
        LEFT, CENTER, RIGHT
    }


    public void control(ThrottleState throttle, SteeringState steering) {
        this.throttle = throttle;
        this.steering = steering;
    }

    @Override
    public void elapseTime(float delta) {
        switch (steering) {
            case LEFT:
                angle = angle + delta * 1f;
                break;
            case RIGHT:
                angle = angle - delta * 1f;
                break;
        }

        float oldSpeed = speed;

        switch (throttle) {
            case FORWARDS:
                speed = Math.min(speed + delta * 25f, 100);
                break;
            case REVERSE:
                speed = Math.max(speed - delta * 25f, -100);
                break;
        }

        if (Math.signum(oldSpeed) == -Math.signum(speed)) {
            speed = 0;
        }

        setPosition(new Vector2(
                getPosition().x + delta * speed * (float) Math.cos(angle),
                getPosition().y + delta * speed * (float) Math.sin(angle)
        ));

        setOrientation(angle);
    }
}

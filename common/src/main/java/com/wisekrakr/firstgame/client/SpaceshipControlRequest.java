package com.wisekrakr.firstgame.client;

import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.io.Serializable;

public class SpaceshipControlRequest implements Serializable {
    private String name;
    private Spaceship.ThrottleState throttleState;
    private Spaceship.SteeringState steeringState;

    public SpaceshipControlRequest(String name, Spaceship.ThrottleState throttleState, Spaceship.SteeringState steeringState) {
        this.name = name;
        this.throttleState = throttleState;
        this.steeringState = steeringState;
    }

    public SpaceshipControlRequest() {
    }

    public Spaceship.ThrottleState getThrottleState() {
        return throttleState;
    }

    public Spaceship.SteeringState getSteeringState() {
        return steeringState;
    }

    public String getName() {
        return name;
    }
}

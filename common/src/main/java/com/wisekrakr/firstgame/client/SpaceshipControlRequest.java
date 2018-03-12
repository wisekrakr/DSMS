package com.wisekrakr.firstgame.client;

import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.io.Serializable;

public class SpaceshipControlRequest implements Serializable {
    private String name;
    private Spaceship.ThrottleState throttleState;
    private Spaceship.SteeringState steeringState;
    private Spaceship.SpecialPowerState specialPowerState;
    private Spaceship.ShootingState shootingState;


    public SpaceshipControlRequest(String name, Spaceship.ThrottleState throttleState, Spaceship.SteeringState steeringState,
                                   Spaceship.SpecialPowerState specialPowerState, Spaceship.ShootingState shootingState) {
        this.name = name;
        this.throttleState = throttleState;
        this.steeringState = steeringState;
        this.specialPowerState = specialPowerState;
        this.shootingState = shootingState;

    }
/*
    public SpaceshipControlRequest(String name, Spaceship.ThrottleState throttleState, Spaceship.SteeringState steeringState, Spaceship.SpecialPowerState specialPowerState, Spaceship.ShootingState shootingState) {
    }
*/
    public Spaceship.ThrottleState getThrottleState() {
        return throttleState;
    }

    public Spaceship.SteeringState getSteeringState() {
        return steeringState;
    }
    public Spaceship.SpecialPowerState getSpecialPowerState() {
        return specialPowerState;
    }

    public Spaceship.ShootingState getShootingState() {
        return shootingState;
    }

    public String getName() {
        return name;
    }
}

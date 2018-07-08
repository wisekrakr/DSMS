package com.wisekrakr.firstgame.client;

import com.wisekrakr.firstgame.engine.gameobjects.Spaceship;

import java.io.Serializable;
import java.util.Objects;

public class SpaceshipControlRequest implements Serializable {
    private String name;
    private Spaceship.ThrottleState throttleState;
    private Spaceship.SteeringState steeringState;
    private Spaceship.SpecialPowerState specialPowerState;
    private Spaceship.ShootingState shootingState;
    private Spaceship.AimingState aimingState;
    private Spaceship.SwitchWeaponState switchWeaponState;
    private Float hardSteering;


    public SpaceshipControlRequest(String name, Spaceship.ThrottleState throttleState, Spaceship.SteeringState steeringState,
                                   Spaceship.SpecialPowerState specialPowerState, Spaceship.ShootingState shootingState,
                                   Spaceship.AimingState aimingState, Spaceship.SwitchWeaponState switchWeaponState, Float hardSteering) {
        this.name = name;
        this.throttleState = throttleState;
        this.steeringState = steeringState;
        this.specialPowerState = specialPowerState;
        this.shootingState = shootingState;
        this.aimingState = aimingState;
        this.switchWeaponState = switchWeaponState;

        this.hardSteering = hardSteering;
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

    public Spaceship.AimingState getAimingState() {
        return aimingState;
    }

    public Spaceship.SwitchWeaponState getSwitchWeaponState() {
        return switchWeaponState;
    }

    public String getName() {
        return name;
    }

    public Float getHardSteering() {
        return hardSteering;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceshipControlRequest that = (SpaceshipControlRequest) o;
        return Objects.equals(name, that.name) &&
                throttleState == that.throttleState &&
                steeringState == that.steeringState &&
                specialPowerState == that.specialPowerState &&
                shootingState == that.shootingState &&
                aimingState == that.aimingState &&
                switchWeaponState == that.switchWeaponState &&
                Objects.equals(hardSteering, that.hardSteering);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, throttleState, steeringState, specialPowerState, shootingState, aimingState, switchWeaponState, hardSteering);
    }
}

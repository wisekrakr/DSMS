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
    private Float mouseAiming;
    private Spaceship.SwitchWeaponState switchWeaponState;
    private Float hardSteering;


    public SpaceshipControlRequest(String name, Spaceship.ThrottleState throttleState, Spaceship.SteeringState steeringState,
                                   Spaceship.SpecialPowerState specialPowerState, Spaceship.ShootingState shootingState,
                                   Float mouseAiming, Spaceship.SwitchWeaponState switchWeaponState, Float hardSteering) {
        this.name = name;
        this.throttleState = throttleState;
        this.steeringState = steeringState;
        this.specialPowerState = specialPowerState;
        this.shootingState = shootingState;
        this.mouseAiming = mouseAiming;
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

    public Float getMouseAiming() {
        return mouseAiming;
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
                Objects.equals(mouseAiming, that.mouseAiming) &&
                switchWeaponState == that.switchWeaponState &&
                Objects.equals(hardSteering, that.hardSteering);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, throttleState, steeringState, specialPowerState, shootingState, mouseAiming, switchWeaponState, hardSteering);
    }
}

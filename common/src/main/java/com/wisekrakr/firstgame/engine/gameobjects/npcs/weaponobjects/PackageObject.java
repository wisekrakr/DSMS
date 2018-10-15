package com.wisekrakr.firstgame.engine.gameobjects.npcs.weaponobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractGameCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.AbstractBehavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.Behavior;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.BehaviorContext;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.behaviors.*;
import com.wisekrakr.firstgame.engine.physicalobjects.PhysicalObject;
import com.wisekrakr.firstgame.engine.physicalobjects.Visualizations;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PackageObject extends AbstractGameCharacter{

    private Vector2 position;
    private float speedMagnitude;
    private float speedDirection;
    private float radius;
    private Visualizations visualizations;
    private boolean timedMission;

    public PackageObject(Vector2 position, float speedMagnitude, float speedDirection, float radius, Visualizations visualizations) {
        this.position = position;
        this.speedMagnitude = speedMagnitude;
        this.speedDirection = speedDirection;
        this.radius = radius;
        this.visualizations = visualizations;
    }


    @Override
    public void start() {
        PhysicalObject packageObject = getContext().addPhysicalObject("package",
                position,
                speedDirection,
                speedMagnitude,
                speedDirection,
                0,
                0,
                visualizations,
                radius,
                null
        );

        getContext().updatePhysicalObjectExtra(packageObject, "radius", radius);

    }

    @Override
    public void elapseTime(float delta) {

    }

    public boolean isTimedMission() {
        return timedMission;
    }
}

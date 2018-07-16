package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectVisualizationType;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.Set;

public class PowerUpMinion extends PowerUp {

    public PowerUpMinion(String name, Vector2 initialPosition) {
        super(GameObjectVisualizationType.POWERUP_MINION, name, initialPosition);
        setCollisionRadius(7.5f);
    }



    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {


    }

}


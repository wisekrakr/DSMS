package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;

public class PowerUpMinion extends PowerUp {

    public PowerUpMinion(String name, Vector2 initialPosition, SpaceEngine space) {
        super(GameObjectType.POWERUP_MINION, name, initialPosition, space);

        setCollisionRadius(30);

    }
/*
    @Override
    public void afterAdd(List<GameObject> toAdd) {
        minionShooterPlayer = new MinionShooterPlayer("minion_shooter", new Vector2(
                getPosition().x + (getCollisionRadius() * 2),
                getPosition().y + (getCollisionRadius() * 2)),
                50,
                getOrientation(), 10, getSpace());

        toAdd.add(minionShooterPlayer);
    }

    @Override
    public void afterRemove(List<GameObject> toAdd, List<GameObject> toRemove) {
        toRemove.add(minionShooterPlayer);
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        minionAngle += 3f * delta;
        minionShooterPlayer.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 45f), (float) (getPosition().y + Math.sin(minionAngle) * 45f)));

    }
    */
}


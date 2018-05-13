package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.MinionShooterPlayer;

import java.util.Set;

public class PowerUpMinion extends PowerUp {

    private MinionShooterPlayer minionShooterPlayer;

    public PowerUpMinion(String name, Vector2 initialPosition, SpaceEngine space) {
        super(name, initialPosition, space);

        setCollisionRadius(30);

        minionShooterPlayer = new MinionShooterPlayer("minion_shooter", new Vector2(
                getPosition().x + (getCollisionRadius() * 2) ,
                getPosition().y + (getCollisionRadius() * 2)),
                50,
                getOrientation() , 10, getSpace());
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

/*
        //minionShooterPlayer.setPosition(minionShooterPlayer.getPosition().sub(getPosition()).rotate(90 * delta).add(getPosition()));
        float angle = angleBetween(minionShooterPlayer, this);
        angle += 3f * delta;
        //minionShooterPlayer.setPosition(new Vector2(getPosition().rotate(angle)));
        minionShooterPlayer.setPosition(new Vector2(minionShooterPlayer.getPosition().rotateRad(1.1f * delta)));
        toAdd.add(minionShooterPlayer);
*/
    }
}


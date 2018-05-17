package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry.MinionShooterPlayer;

import java.util.List;
import java.util.Set;

public class PowerUpMinion extends PowerUp {
    private MinionShooterPlayer minionShooterPlayer;
    private float minionAngle = 0;

    public PowerUpMinion(String name, Vector2 initialPosition, SpaceEngine space) {
        super(GameObjectType.POWERUP_MINION, name, initialPosition, space);

        setCollisionRadius(30);

    }

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


        //minionShooterPlayer.setPosition(minionShooterPlayer.getPosition().sub(getPosition()).rotate(90 * delta).add(getPosition()));

        minionAngle += 3f * delta;
//        minionShooterPlayer.setPosition(new Vector2(getPosition().rotate(angle)));

        minionShooterPlayer.setPosition(new Vector2((float) (getPosition().x + Math.cos(minionAngle) * 45f), (float) (getPosition().y + Math.sin(minionAngle) * 45f)));
//        toAdd.add(minionShooterPlayer);

    }
}


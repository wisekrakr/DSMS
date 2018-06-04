package com.wisekrakr.firstgame.engine.gameobjects.powerups;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;

import java.util.Random;
import java.util.Set;

public class PowerupGenerator extends GameObject {
    public PowerupGenerator(Vector2 initialPosition, SpaceEngine space) {
        super(GameObjectType.POWERUP_GENERATOR, "Powerup generator", initialPosition, space);
    }

    private float time;

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        Random random = new Random();

        time += delta;
        if (time >= random.nextFloat() * 5000f) {
            int randomPowerUp = MathUtils.random(3, 3);
            switch (randomPowerUp) {
                case 1:
                    PowerUpMissile powerUpMissile = new PowerUpMissile("missile power up", new Vector2(random.nextFloat() * 5000 - 3000,
                            random.nextFloat() * 5000 - 3000), getSpace());
                    toAdd.add(powerUpMissile);
                    break;
                case 2:
                    PowerUpShield powerUpShield = new PowerUpShield("shield power up", new Vector2(random.nextFloat() * 5000 - 3000,
                            random.nextFloat() * 5000 - 3000), getSpace());
                    toAdd.add(powerUpShield);
                    break;
                case 3:
                    PowerUpMinion powerUpMinion = new PowerUpMinion("minion power up", new Vector2(random.nextFloat() * 5000 - 3000,
                            random.nextFloat() * 5000 - 3000), getSpace());
                    toAdd.add(powerUpMinion);
                    break;
                case 4:
                    PowerUpHealth powerUpHealth = new PowerUpHealth("health", new Vector2(random.nextFloat() * 5000 - 3000,
                            random.nextFloat() * 5000 - 3000), getSpace());
                    toAdd.add(powerUpHealth);
                    break;
            }

            time = 0;
        }
    }
}

package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gamecharacters.AbstractNonPlayerGameCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.AsteroidCharacter;
import com.wisekrakr.firstgame.engine.gamecharacters.StandardAggressiveCharacter;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.AsteroidWatchingMissileShootingNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.CrazilySpawningPassiveAggressiveNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.FollowingChasingNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.TestNPC;
import com.wisekrakr.firstgame.engine.scenarios.CharacterFactory;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;

public class ScenarioHelper {

    public static final GameObjectFactory CRAZY_SPAWNER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new CrazilySpawningPassiveAggressiveNPC(initialPosition, actionDistance);
                }
            };

    public static final GameObjectFactory MISSILE_SHOOTER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new AsteroidWatchingMissileShootingNPC(initialPosition, actionDistance);
                }
            };

    public static final GameObjectFactory TEST_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new TestNPC(initialPosition, actionDistance);
                }
            };

    public static final GameObjectFactory CHASING_SHOOTING_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new FollowingChasingNPC(initialPosition, actionDistance);
                }
            };



    public static final CharacterFactory STANDARD_CHARACTER_FACTORY =
            new CharacterFactory() {
                @Override
                public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                    return new StandardAggressiveCharacter(position, radius, speedDirection, speedMagnitude, radiusOfAttack, health);
                }
            };

    public static final CharacterFactory ASTEROID_FACTORY =
            new CharacterFactory() {
                @Override
                public AbstractNonPlayerGameCharacter createCharacter(Vector2 position, float speedMagnitude, float orientation, float speedDirection, float radius, float radiusOfAttack, float health, float damage) {
                    return new AsteroidCharacter(position, radius, speedDirection, speedMagnitude, health, damage);
                }
            };

}

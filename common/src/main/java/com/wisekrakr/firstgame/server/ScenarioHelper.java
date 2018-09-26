package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.KillMission;
import com.wisekrakr.firstgame.engine.gameobjects.missions.sidemissions.PackageDeliveryMission;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.AsteroidWatchingMissileShootingNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.CrazilySpawningPassiveAggressiveNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.FollowingChasingNPC;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.TestNPC;
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


    public static final GameObjectFactory KILL_MISSION =
            new GameObjectFactory() {

                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {

                    return new KillMission(initialPosition, "");
                }

            };

    public static final GameObjectFactory PACKAGE_MISSION =
            new GameObjectFactory() {

                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {

                    return new PackageDeliveryMission(initialPosition, "");
                }

            };

}

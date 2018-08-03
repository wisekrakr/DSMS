package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.enemyversions.*;
import com.wisekrakr.firstgame.engine.gameobjects.npcs.gameobjects.CrazilySpawningPassiveAggressiveNPC;
import com.wisekrakr.firstgame.engine.scenarios.GameObjectFactory;
import com.wisekrakr.firstgame.engine.scenarios.SwarmScenario;

import static com.badlogic.gdx.math.MathUtils.random;

public class ScenarioHelper {
    public static SwarmScenario CREATE_PEST_SWARM() {
        return new SwarmScenario(6, 20,
                position -> new EnemyPest("Pest", position,
                        20, random.nextFloat() * 2000,
                        50f, 3.5f));
    }


    public static final GameObjectFactory CHASER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyChaser("Chaser",
                            initialPosition,
                            70, initialDirection,
                            50f, 5.5f);
                }
            };

    public static final GameObjectFactory HOMER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyHomer("Homer",
                            initialPosition,
                            75, initialDirection,
                            43.75f, 7.5f);
                }
            };

    public static final GameObjectFactory SHITTER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyShitter("Shitter",
                            initialPosition,
                            80, initialDirection,
                            37.5f, 7.5f);
                }
            };

    public static final GameObjectFactory EWM_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyWithMinion("E.L.S.",
                            initialPosition,
                            100, initialDirection,
                            47.5f, 6.5f);
                }
            };

    public static final GameObjectFactory PEST_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyPest("Pest",
                            initialPosition,
                            25, initialDirection,
                            57.25f, 3.5f);
                }
            };

    public static final GameObjectFactory MOTHER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyMotherShip("Mother",
                            initialPosition,
                            200, initialDirection,
                            7.25f, 37.5f);
                }
            };

    public static final GameObjectFactory MUTATOR_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyMutator("Mutator",
                            initialPosition,
                            150, initialDirection,
                            17.25f, 23.5f);
                }
            };

    public static final GameObjectFactory BLINKER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyBlinker("Blinker",
                            initialPosition,
                            50, initialDirection,
                            43.75f, 6.5f);
                }
            };

    public static final GameObjectFactory DODGER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyDodger("Dodger",
                            initialPosition,
                            10, initialDirection,
                            31.75f, 3.5f);
                }
            };

    public static final GameObjectFactory FACEHUGGER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyFaceHugger("Facehugger",
                            initialPosition,
                            40, initialDirection,
                            50f, 2.5f);
                }
            };

    public static final GameObjectFactory SHOTTY_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new EnemyShotty("Shotgun",
                            initialPosition,
                            25, initialDirection,
                            32f, 3.5f);
                }
            };

    public static final GameObjectFactory CRAZY_SPAWNER_FACTORY =
            new GameObjectFactory() {
                @Override
                public GameObject create(Vector2 initialPosition, float initialDirection, float actionDistance) {
                    return new CrazilySpawningPassiveAggressiveNPC(initialPosition);
                }
            };
}

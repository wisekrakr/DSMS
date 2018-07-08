package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyChaser;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyPest;
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
                public GameObject create(Vector2 initialPosition, float initialDirection) {
                    return new EnemyChaser("Chaser",
                            initialPosition,
                            70, initialDirection,
                            50f, 5.5f);
                }
            };

}

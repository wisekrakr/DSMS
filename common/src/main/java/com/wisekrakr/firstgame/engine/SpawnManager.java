package com.wisekrakr.firstgame.engine;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyChaser;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyShitter;
import com.wisekrakr.firstgame.engine.scenarios.Scenario;
import com.wisekrakr.firstgame.server.EngineConstants;

import java.util.ArrayList;
import java.util.Random;

public class SpawnManager {

    private SpaceEngine engine;

    private int targetCount;

    private Random random = new Random();
    private ArrayList<GameObject>enemies = new ArrayList<>();

    public SpawnManager(int targetCount) {
        this.targetCount = targetCount;

    }

    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (targetCount > enemies.size() ) {

            GameObject gameObject = addChaser();

            spaceEngine.addGameObject(gameObject, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    enemies.add(gameObject);
                }

                @Override
                public void removed() {
                    enemies.remove(gameObject);
                }
            });
        }
    }

    public EnemyChaser addChaser(){
        return new EnemyChaser("Chaser", randomPosition(),
                70, randomDirection(),
                50f, 5.5f);
    }

    public EnemyShitter addShitter(){
        return new EnemyShitter("Shitter", randomPosition(),
                80,randomDirection(),
                37.5f,7.5f);
    }

    private Vector2 randomPosition() {
        return new Vector2(random.nextFloat() * EngineConstants.ENGINE_WIDTH - EngineConstants.PLUS_XY,
                random.nextFloat() * EngineConstants.ENGINE_HEIGHT - EngineConstants.PLUS_XY);
    }

    private float randomDirection(){
        return random.nextFloat() * 2000 - 1000;
    }


}

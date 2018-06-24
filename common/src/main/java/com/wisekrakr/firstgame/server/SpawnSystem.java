package com.wisekrakr.firstgame.server;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyChaser;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyShitter;

import java.util.ArrayList;
import java.util.Random;

public class SpawnSystem {

    private final SpaceEngine engine;

    private Random random = new Random();
    private ArrayList<Enemy>enemies = new ArrayList<>();

    public SpawnSystem(SpaceEngine engine) {
        this.engine = engine;

        enemies.add(addChaser());
        enemies.add(addShitter());

    }

    public EnemyChaser addChaser(){
        return new EnemyChaser("Chaser", new Vector2(
                random.nextFloat() * EngineConstants.ENGINE_WIDTH - EngineConstants.PLUS_XY,
                random.nextFloat() * EngineConstants.ENGINE_HEIGHT - EngineConstants.PLUS_XY),
                70, random.nextFloat() * 2000 - 1000,
                50f, 5.5f);
    }

    public EnemyShitter addShitter(){
        return new EnemyShitter("Shitter", new Vector2(
                random.nextFloat() * EngineConstants.ENGINE_WIDTH - EngineConstants.PLUS_XY,
                random.nextFloat() * EngineConstants.ENGINE_HEIGHT - EngineConstants.PLUS_XY),
                80,random.nextFloat() * 2000 - 1000,
                37.5f,7.5f);
    }


    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}

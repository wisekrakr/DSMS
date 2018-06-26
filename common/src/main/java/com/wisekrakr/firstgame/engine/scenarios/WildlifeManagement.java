package com.wisekrakr.firstgame.engine.scenarios;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.EnemyMotherShip;
import com.wisekrakr.firstgame.server.EngineConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class WildlifeManagement extends Scenario {
    private float minCreationInterval;
    private float lastCreation = 0f;
    private final Function<Vector2, GameObject> factory;
    private int targetCount;
    private List<GameObject> myObjects = new ArrayList<>();
    private Random randomGenerator = new Random();

    public WildlifeManagement(int targetCount, float minCreationInterval, Function<Vector2, GameObject> factory) {
        this.targetCount = targetCount;
        this.minCreationInterval = minCreationInterval;
        this.factory = factory;
    }

    public void periodicUpdate(SpaceEngine spaceEngine) {
        if (targetCount > myObjects.size() && lastCreation + minCreationInterval <= spaceEngine.getTime()) {
            lastCreation = spaceEngine.getTime();

            GameObject newObject = factory.apply(randomPosition());

            spaceEngine.addGameObject(newObject, new SpaceEngine.GameObjectListener() {
                @Override
                public void added() {
                    myObjects.add(newObject);
                }

                @Override
                public void removed() {
                    myObjects.remove(newObject);
                }
            });
        }
//TODO: If we kill a certain number a boss wil arise
/*
        if (myObjects.size() <= 0){
            spaceEngine.addGameObject(new EnemyMotherShip("MotherShip", randomPosition(), 200,
                    randomDirection(), 7.5f, 40f));
        }
        */
    }

    private Vector2 randomPosition() {
        return new Vector2(randomGenerator.nextFloat() * EngineConstants.ENGINE_WIDTH - EngineConstants.PLUS_XY,
                randomGenerator.nextFloat() * EngineConstants.ENGINE_HEIGHT - EngineConstants.PLUS_XY);
    }

    private float randomDirection(){
        return randomGenerator.nextFloat() * 2000 - 1000;
    }

/*



        engine.addGameObject(new PowerupGenerator(new Vector2(
            randomGenerator.nextFloat() * width - plusOfXY,
            randomGenerator.nextFloat() * height - plusOfXY),
    engine));

        engine.addGameObject(new QuestGen(new Vector2(
            randomGenerator.nextFloat() * width - plusOfXY,
            randomGenerator.nextFloat() * height - plusOfXY),
    engine));


        for (int i = 0; i < 4; i++) {
        EnemyChaser chaser = new EnemyChaser("Chaser", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                70, randomGenerator.nextFloat() * 2000 - 1000,
                50f, 5.5f, engine);
        engine.addGameObject(chaser);
    }

        for (int i = 0; i < 2; i++) {
        EnemyShitter shitter = new EnemyShitter("Shitter", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                80,randomGenerator.nextFloat() * 2000 - 1000,
                37.5f,7.5f, engine);
        engine.addGameObject(shitter);
    }

        for (int i = 0; i < 2; i++) {
        EnemyEls enemyEls = new EnemyEls("ELS", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                100,randomGenerator.nextFloat() * 2000 - 1000,
                47.5f, 6.25f, engine);
        engine.addGameObject(enemyEls);
    }
        for (int i = 0; i < 20; i++) {
        Asteroid asteroid = new Asteroid("Boeja", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                randomGenerator.nextFloat() * 20,
                randomGenerator.nextFloat() * 80,
                randomGenerator.nextFloat() * 2 * (float) Math.PI, engine,
                randomGenerator.nextFloat() * 5f);
        engine.addGameObject(asteroid);
    }


*/

    /*
        for (int i = 0; i < 5; i++) {
            EnemyBlinker enemyBlinker = new EnemyBlinker("Blinker", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    50,randomGenerator.nextFloat() * 2000 - 1000,
                    43.75f,6.25f, engine);
            engine.addGameObject(enemyBlinker);
        }



        for(int i = 0; i < 5; i++){
            EnemyHomer enemyHomer = new EnemyHomer("MissileEnemy", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    75,randomGenerator.nextFloat() * 2000 - 1000,
                    43.75f,7.5f, engine);
            engine.addGameObject(enemyHomer);
        }


        for (int i = 0; i < 5; i++) {
            EnemyPest pest = new EnemyPest("Pest", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    25,randomGenerator.nextFloat() * 2000 - 1000,
                    57.5f,3.75f, engine);
            engine.addGameObject(pest);
        }

        EnemyMotherShip enemyMotherShip = new EnemyMotherShip("MotherShip", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                200, randomGenerator.nextFloat() * 2000 - 1000,
                7.5f,37.5f, engine);
        engine.addGameObject(enemyMotherShip);

        EnemyMutator enemyMutator = new EnemyMutator("SporeShip", new Vector2(
                randomGenerator.nextFloat() * width - plusOfXY,
                randomGenerator.nextFloat() * height - plusOfXY),
                150, randomGenerator.nextFloat() * 2000 - 1000,
                12.5f,25f, engine);
        engine.addGameObject(enemyMutator);


        for (int i = 0; i < 4; i++) {
            EnemyFaceHugger enemyFaceHugger = new EnemyFaceHugger("Face Hugger", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    40, randomGenerator.nextFloat() * 2000 - 1000,
                    50f, 2f, engine);
            engine.addGameObject(enemyFaceHugger);
        }

        for (int i = 0; i < 10; i++) {
            EnemyDodger enemyDodger = new EnemyDodger("Dodger", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    10,randomGenerator.nextFloat() * 2000 - 1000 ,
                    31.25f,3f, engine);
            engine.addGameObject(enemyDodger);
        }

       for(int i = 0; i < 10; i++){
            EnemyShotty enemyShotty = new EnemyShotty("Stalker", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    25,randomGenerator.nextFloat() * 2000 - 1000,
                    32f,3f, engine);
            engine.addGameObject(enemyShotty);
        }

        for (int i = 0; i < 5; i++) {
            EnemyGang enemyGang = new EnemyGang("Gang!", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    50,randomGenerator.nextFloat() * 2000 - 1000,
                    120f,2.5f, engine);
            engine.addGameObject(enemyGang);
        }

        for (int i = 0; i < 1; i++) {
            Rotunda rotunda = new Rotunda("rotunda test", new Vector2(
                    randomGenerator.nextFloat() * width - plusOfXY,
                    randomGenerator.nextFloat() * height - plusOfXY),
                    engine, 12.5f, randomGenerator.nextFloat() * 2 * (float) Math.PI);
            engine.addGameObject(rotunda);
        }
*/

}

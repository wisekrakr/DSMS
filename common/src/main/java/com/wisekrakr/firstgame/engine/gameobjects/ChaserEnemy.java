package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.Random;

public class ChaserEnemy extends GameObject {
    private static final float DEFAULT_ENEMY_SPEED = 20;
    private static final float AGRO_DISTANCE = 150;
    private static final int CHANGE_DIRECTION_TIME = 3000;

    public ChaserEnemy(String name, Vector2 position, SpaceEngine space) {
        super(name, position, space);

        setCollisionRadius(10);
    }



    public void attack(float delta) {
        /*
        Vector2 newPosition = getPosition();
        Vector2 direction = new Vector2();
        Vector2 movingPosition = new Vector2();

        direction.set(getPlayer1().getPosition()).sub(newPosition).nor();
        setVelocity(direction.scl(DEFAULT_ENEMY_SPEED));
        movingPosition.set(getVelocity()).scl(delta);
        if (newPosition.dst2(getPlayer1().getPosition()) > movingPosition.len2()) {
            newPosition.add(movingPosition);

        } else {
            newPosition.set(getPlayer1().getPosition());
        }
*/

    }

    public void movement(float delta) {
        Vector2 newPosition = getPosition();
        Vector2 direction = new Vector2();

        Random randomPositionMaker = new Random(500);

        Vector2 randomPosition = new Vector2();
        Vector2 movingPosition = new Vector2();

        direction.set(newPosition.sub(randomPosition)).nor();
        //setVelocity(direction.scl(DEFAULT_ENEMY_SPEED));
//        movingPosition.set(getVelocity()).scl(delta);
        newPosition.add(movingPosition);


    }


    @Override
    public void elapseTime(float delta) {
/*
        if(collisionDetector(getPlayer1()) < AGRO_DISTANCE){
            attack(delta);
        }else {
            movement(delta);

        }
*/

    }


}

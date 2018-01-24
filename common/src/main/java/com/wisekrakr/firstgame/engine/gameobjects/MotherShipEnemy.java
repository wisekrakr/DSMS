package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MotherShipEnemy extends Enemy{
    private static final float DEFAULT_ENEMY_SPEED = 5;
    private static final float AGRO_DISTANCE = 150;
    private static final int CHANGE_DIRECTION_TIME = 3000;
    private float direction;
    private float radius;

    public MotherShipEnemy(String name, Vector2 position, float direction, float radius, SpaceEngine space) {
        super(name, position, direction, radius, space);
        this.direction = direction;
        this.radius = radius;

        setCollisionRadius(radius);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.signalOutOfBounds(toDelete, toAdd);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {


        if (subject instanceof Asteroid) {
            radius = radius - ((Asteroid) subject).getRadius();
            setCollisionRadius(radius);
            toDelete.remove(subject);
        }
        if(subject instanceof Enemy){
            toAdd.add(subject);

        }

    }

    public void addMinions(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd){

        Enemy enemy = new Enemy("Minion1", subject.getPosition(), getDirection(), getRadius(), this.getSpace());

        if (subject instanceof Player) {
            if(this.getPosition().x + this.getRadius() == subject.getPosition().x){
                toAdd.add(enemy);
            }
        }

    }

    @Override
    public void attack(GameObject target) {

        if (target instanceof Player) {

            if (distanceBetween(this, target) <= AGRO_DISTANCE ) {
                float playerPosition = (float) Math.hypot(target.getPosition().x, target.getPosition().y);

                setOrientation(playerPosition);
                setDirection(playerPosition);

            }
        }
    }



    public float changeDirection(){

        Random randomGenerator = new Random();
        float newDirection = 0;
        newDirection = randomGenerator.nextFloat();

        direction = (float) Math.atan(direction - newDirection);

        return direction;
    }


    @Override
    public void elapseTime(float delta) {


        setPosition(new Vector2(getPosition().x + (float) Math.cos(changeDirection()) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(changeDirection()) * DEFAULT_ENEMY_SPEED * delta)
        );

        setOrientation(changeDirection());



    }

    public float getDirection() {
        return direction;
    }

    public float getRadius() {
        return radius;
    }
    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }
}

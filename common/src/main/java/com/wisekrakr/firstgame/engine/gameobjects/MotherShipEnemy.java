package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MotherShipEnemy extends Enemy{
    private static final float DEFAULT_ENEMY_SPEED = 10;
    private static final float AGRO_DISTANCE = 450;
    private static final float ATTACK_DISTANCE = 150;
    private static final int CHANGE_DIRECTION_TIME = 3000;
    private float direction;
    private float radius;
    private int minionCount;

    private AttackState attackState = AttackState.PACIFIST;

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
        toDelete.add(subject);

        if (subject instanceof Enemy) {
            radius = radius - ((Enemy) subject).getRadius();
            setCollisionRadius(radius);
            toDelete.remove(subject);
        }


    }

    @Override
    public void targetSpotted(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player) {

            if (distanceBetween(this, subject) <= AGRO_DISTANCE ) {
                float angle = angleBetween(this, subject);

                // to make the chaser chase the player with less vigilance, divide cos and sin by 2
                setPosition(new Vector2(getPosition().x +=  Math.cos(angle) /2 , getPosition().y +=  Math.sin(angle)/2 ));

                setOrientation(angle);

                setDirection(angle);

            }
        }

    }

    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Player) {

            if (distanceBetween(this, subject) <= ATTACK_DISTANCE ) {
                float angle = angleBetween(this, subject);
                minionCount = 4;

                for(int i = 0; i < minionCount; i++) {
                    toAdd.add(new ChaserEnemy("ChaserMinion1", new Vector2(getPosition().x + 40, getPosition().y), angle, 10f, getSpace()));
                }

                setOrientation(angle);

                setDirection(angle);

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
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {


        setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(getDirection()) * DEFAULT_ENEMY_SPEED * delta)
        );

        setOrientation(changeDirection());

        switch (attackState){
            case SHOOT:

                break;
            case CHASE:

                break;
            case SELF_DESTRUCT:
                toDelete.add(this);
        }



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

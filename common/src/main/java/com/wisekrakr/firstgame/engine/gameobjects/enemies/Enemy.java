package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Debris;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Enemy extends GameObject {

    private float direction;
    private float radius;
    private int health;
    private float speed;
    private float attackDistance;
    private float aggroDistance;

    private static final float CLOSEST_TARGET = 100;
    private AttackState attackstate = AttackState.PACIFIST;


    public Enemy(String name, Vector2 position, int health, float direction, float speed, float radius, SpaceEngine space) {
        super(name, position, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        this.speed = speed;

        setHealth(health);
        setCollisionRadius(radius);
        setSpeed(speed);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
       this.setDirection(direction + (float) Math.PI);
    }

    @Override
    public void overlappingObjects(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        Random random = new Random();
        if(subject instanceof Enemy){
            float angle = angleBetween(this, subject);
            if(distanceBetween(this, subject)<= getCollisionRadius() + subject.getCollisionRadius()) {
                setPosition(new Vector2(getPosition().x -= Math.cos(angle) * random.nextFloat() * 1.5,
                        getPosition().y -= Math.sin(angle) * random.nextFloat() * 1.5));
                setOrientation(-angle);
                setDirection(direction + (float) Math.PI);
            }
        }
    }

    public enum AttackState {
        PACIFIST, CHASE, SHOOT, SELF_DESTRUCT;
    }

    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        Random random = new Random();
        if(distanceBetween(this, subject) <= 300){
            float angle = angleBetween(this, subject);
            setPosition(new Vector2(getPosition().x -=  Math.cos(angle) * random.nextFloat() * 3 ,
                    getPosition().y -=  Math.sin(angle) * random.nextFloat() * 3 ));
            setOrientation(angle);
            setDirection(angle);
        }
    }

    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(target instanceof Player){
            if(distanceBetween(this, target)< CLOSEST_TARGET){
                attackTarget(target, toDelete, toAdd);
            }
        }
    }


    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (health <= 0) {
            toDelete.add(this);
            Random random = new Random();
            int debrisParts = random.nextInt(10)+1;
            for(int i = 0; i < debrisParts; i++) {
                toAdd.add(new Debris("debris", this.getPosition(), getSpace(), random.nextFloat() * 10,
                        random.nextFloat() * 30, random.nextFloat() * 2 * (float) Math.PI, random.nextFloat() * getRadius()));

            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(float attackDistance) {
        this.attackDistance = attackDistance;
    }

    public float getAggroDistance() {
        return aggroDistance;
    }

    public void setAggroDistance(float aggroDistance) {
        this.aggroDistance = aggroDistance;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }





}

package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.GameObjectType;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.spaceobjects.Asteroid;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MinionShooterEnemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MissilePlayer extends GameObject {

    private AttackState attackState = AttackState.NONE;
    private float direction;
    private float radius;
    private float speed;
    private int damage;
    private float time;
    private float attackDistance;

    public MissilePlayer(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(GameObjectType.MISSILE, name, initialPosition, space);

        this.direction = direction;
        this.radius = radius;
        this.speed = speed;
        this.damage = damage;

        setCollisionRadius(radius);
        setSpeed(speed);
        setAttackDistance(500f);
    }

    public enum AttackState{
        NONE, HOMING
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(subject instanceof Asteroid){
            toDelete.add(this);
            toDelete.add(subject);
        }
        if(subject instanceof Enemy){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
        }
        if (subject instanceof MinionShooterEnemy){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());

        }
    }

    @Override
    public void attackTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (target instanceof Enemy) {
            if (distanceBetween(this, target) <= getAttackDistance()) {
                float angle = angleBetween(this, target);
                setAttackState(AttackState.HOMING);
                setOrientation(angle);
                setDirection(angle);
            }else {
                setAttackState(AttackState.NONE);
            }
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        float destructTime = 3.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }

        switch (attackState){
            case NONE:
                setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * getSpeed() * delta,
                        getPosition().y + (float) Math.sin(direction) * getSpeed() * delta)
                );
                setOrientation(direction);
                break;
            case HOMING:
                setPosition(new Vector2(getPosition().x + (float) Math.cos(getOrientation()) * getSpeed() * delta,
                        getPosition().y + (float) Math.sin(getOrientation()) * getSpeed() * delta)
                );
                setDirection((float) (getOrientation() * Math.PI));
                break;
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public AttackState getAttackState() {
        return attackState;
    }

    public void setAttackState(AttackState attackState) {
        this.attackState = attackState;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }


    public float getAttackDistance() {
        return attackDistance;
    }

    public void setAttackDistance(float attackDistance) {
        this.attackDistance = attackDistance;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }

    @Override
    public Map<String, Object> getDamageProperties() {
        Map<String, Object> result = new HashMap<>();

        result.put("damage", damage);

        return result;
    }

}

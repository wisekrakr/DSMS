package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.AutonomousWeaponsPlayer;

import java.util.Set;

public class MissilePlayer extends AutonomousWeaponsPlayer {

    private float direction;
    private float radius;
    private float speed;
    private int damage;
    private float time;

    private static final float ATTACK_RANGE = 300;
    private static final float DEFAULT_MISSILE_SPEED = 550;


    public MissilePlayer(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(name, initialPosition, space, direction, radius, damage);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;
        this.damage = damage;

        setCollisionRadius(radius);
    }


    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.collide(subject, toDelete, toAdd);
        if(subject instanceof Enemy){
            toDelete.add(this);
            subject.setHealth(subject.getHealth() - getDamage());
        }
    }

    @Override
    public void getClosestTarget(GameObject target, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if(target instanceof Enemy) {
            if(distanceBetween(this, target)< ATTACK_RANGE){
                attackTarget(target, toDelete, toAdd);
            }
        }

    }

    @Override
    public void attackTarget(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        if (subject instanceof Enemy) {
            float angle = angleBetween(this, subject);
            setPosition(new Vector2(getPosition().x += Math.cos(angle), getPosition().y += Math.sin(angle)));
            setOrientation(angle);
            setDirection(angle);
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_MISSILE_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_MISSILE_SPEED * delta)
        );
        setOrientation(direction);

        float destructTime = 3.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }
}

package com.wisekrakr.firstgame.engine.gameobjects.weaponry.playerweaponry;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.enemies.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.AutonomousWeaponsPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.enemyweaponry.MinionShooterEnemy;

import java.util.Set;

public class MissilePlayer extends AutonomousWeaponsPlayer {

    private float direction;
    private float radius;
    private float speed;
    private int damage;
    private float time;

    private static final float ATTACK_RANGE = 400;

    public MissilePlayer(String name, Vector2 initialPosition, SpaceEngine space, float direction, float speed, float radius, int damage) {
        super(name, initialPosition, space, direction, radius, damage, speed);
        this.direction = direction;
        this.radius = radius;
        this.speed = speed;
        this.damage = damage;

        setCollisionRadius(radius);
        setSpeed(speed);
        setAttackDistance(500f);
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
        super.elapseTime(clock, delta, toDelete, toAdd);
        float destructTime = 3.0f;
        time += delta;
        if(time >= destructTime){
            toDelete.add(this);
        }
    }

}

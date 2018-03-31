package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.BulletPlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.MissilePlayer;
import com.wisekrakr.firstgame.engine.gameobjects.weaponry.Spores;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class EnemyMutator extends Enemy {

    private float DEFAULT_ENEMY_SPEED = 30;
    private static final float AGRO_DISTANCE = 800;
    private static final float ATTACK_DISTANCE = 550;
    private static final int CHANGE_DIRECTION_TIME = 3000;
    private float direction;
    private float radius;
    private int health;
    private float shotLeftOver;
    private int ammoCount;
    private AttackState attackState = AttackState.PACIFIST;

    public EnemyMutator(String name, Vector2 position, int health, float direction, float radius, SpaceEngine space) {
        super(name, position, health, direction, radius, space);
        this.direction = direction;
        this.radius = radius;
        this.health = health;
        ammoCount = 10000;
        shotLeftOver = ammoCount;

        setCollisionRadius(radius);
        setHealth(health);

    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.signalOutOfBounds(toDelete, toAdd);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if(subject instanceof BulletPlayer){
            radius = radius - subject.getCollisionRadius();
            setCollisionRadius(radius);
            toDelete.add(subject);
        }
        if(subject instanceof MissilePlayer){
            radius = radius - subject.getCollisionRadius();
            setCollisionRadius(radius);
            toDelete.add(subject);
        }
        if(subject instanceof Player){
            toDelete.add(subject);
        }
    }

    @Override
    public void elapseTime(float clock, float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.elapseTime(clock, delta, toDelete, toAdd);
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_ENEMY_SPEED * delta)
        );
        setOrientation(direction);

        switch (attackState){
            case SHOOT:

                ammoCount = getAmmoCount();
                float shotCount = delta / 0.09f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    Random randomGenerator = new Random();
                    toAdd.add(new Spores("spores", new Vector2(getPosition().x + randomGenerator.nextFloat() * radius,
                            getPosition().y + randomGenerator.nextFloat() * radius),
                            getSpace(), getOrientation(), 1f));

                }

                break;
            case PACIFIST:
                shotLeftOver = 0;
                break;
        }
    }

    public int getAmmoCount() {
        return ammoCount;
    }


    @Override
    public float getDirection() {
        return super.getDirection();
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        return super.getExtraSnapshotProperties();
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
        super.attackTarget(subject, toDelete, toAdd);
        if (subject instanceof Player) {

            if (distanceBetween(this, subject) <= ATTACK_DISTANCE ) {

                attackState = AttackState.SHOOT;
            }else{
                attackState = AttackState.PACIFIST;
            }
        }
    }
}

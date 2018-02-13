package com.wisekrakr.firstgame.engine.gameobjects.enemies;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;
import com.wisekrakr.firstgame.engine.gameobjects.Enemy;
import com.wisekrakr.firstgame.engine.gameobjects.GameObject;
import com.wisekrakr.firstgame.engine.gameobjects.Player;
import com.wisekrakr.firstgame.engine.gameobjects.Weapons;

import java.util.*;

public class MotherShipEnemy extends Enemy {
    private static final float DEFAULT_ENEMY_SPEED = 30;
    private static final float AGRO_DISTANCE = 1250;
    private static final float ATTACK_DISTANCE = 750;
    private static final int CHANGE_DIRECTION_TIME = 3000;
    private float direction;
    private float radius;
    private int ammoCount;

    private AttackState attackState = AttackState.PACIFIST;
    private float shotLeftOver;
    private float time;


    public MotherShipEnemy(String name, Vector2 position, float direction, float radius, SpaceEngine space) {
        super(name, position, direction, radius, space);
        this.direction = direction;
        this.radius = radius;
        ammoCount = 6;
        shotLeftOver = ammoCount;
        setCollisionRadius(radius);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.signalOutOfBounds(toDelete, toAdd);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {


        if (subject instanceof DodgingEnemy) {
            radius = radius + ((DodgingEnemy) subject).getRadius();
            setCollisionRadius(radius);
            toDelete.add(subject);
        }
        if(subject instanceof Weapons){
            radius = radius - subject.getCollisionRadius();
            setCollisionRadius(radius);
            toDelete.add(subject);
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
                attackState = AttackState.SHOOT;
            }else{
                attackState = AttackState.PACIFIST;
            }

        }
    }





    public float changeDirection(){

        Random randomGenerator = new Random();
        float newDirection = randomGenerator.nextFloat();

        direction = (float) Math.atan(direction - newDirection);

        return direction;
    }


    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {


        setPosition(new Vector2(getPosition().x + (float) Math.cos(getDirection()) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(getDirection()) * DEFAULT_ENEMY_SPEED * delta)
        );

        setOrientation(getDirection());

        switch (attackState){
            case SHOOT:
                ammoCount = getAmmoCount();
                float shotCount = delta / 1.5f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for(int i = 0; i < exactShotCount; i++) {
                    Random randomGenerator = new Random();
                    ChaserEnemy chaserEnemy = new ChaserEnemy("ChaserMinion1", new Vector2(
                            getPosition().x + randomGenerator.nextFloat() * radius,
                            getPosition().y + randomGenerator.nextFloat() * radius),
                            getDirection(), 10f, getSpace());
                    toAdd.add(chaserEnemy);

                    float destructTime = 8.0f;
                    time += delta;
                    if(time >= destructTime){
                        float angle = angleBetween(this, chaserEnemy);

                        // to make the chaser chase the player with less vigilance, divide cos and sin by 2
                        chaserEnemy.setPosition(new Vector2(this.getPosition().x +=  Math.cos(angle)  , this.getPosition().y +=  Math.sin(angle) ));

                        chaserEnemy.setOrientation(angle);

                        chaserEnemy.setDirection(angle);

                    }
                }

                break;

            case PACIFIST:
                shotLeftOver = 0;
        }



    }

    public int getAmmoCount() {
        return ammoCount;
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

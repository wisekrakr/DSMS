package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.Map;
import java.util.Set;

public class MissileEnemy extends Enemy {

    private float DEFAULT_ENEMY_SPEED = 80;
    private static final float AGRO_DISTANCE = 450;
    private static final float ATTACK_DISTANCE = 400;

    private float direction;
    private float radius;
    private float shotLeftOver;
    private int ammoCount;
    private AttackState attackState = AttackState.PACIFIST;


    public MissileEnemy(String name, Vector2 position, float direction, float radius, SpaceEngine space) {
        super(name, position, direction, radius, space);
        this.direction = direction;
        this.radius = radius;

        ammoCount = 10;
        shotLeftOver = ammoCount;
        setCollisionRadius(radius);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.signalOutOfBounds(toDelete, toAdd);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        //toDelete.add(subject);

        if (subject instanceof Asteroid) {
            toDelete.add(this);
        }

        if(subject instanceof Bullet){
            toDelete.add(this);
        }
    }

    @Override
    public void elapseTime(float delta, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        setPosition(new Vector2(getPosition().x + (float) Math.cos(direction) * DEFAULT_ENEMY_SPEED * delta,
                getPosition().y + (float) Math.sin(direction) * DEFAULT_ENEMY_SPEED * delta)
        );
        setOrientation(direction);

        switch (attackState) {
            case SHOOT:
                ammoCount = getAmmoCount();
                float shotCount = delta / 2f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(new Missile("missile", new Vector2(getPosition().x + 16, getPosition().y + 16),
                            getOrientation(), 4f, getSpace()));
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
    public float getRadius() {
        return super.getRadius();
    }


    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        return super.getExtraSnapshotProperties();
    }

    @Override
    public void nothingSpotted(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player) {

            if ((distanceBetween(this, subject) > AGRO_DISTANCE)) {

                attackState = AttackState.PACIFIST;
            }
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
            }
        }
    }
}

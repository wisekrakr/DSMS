package com.wisekrakr.firstgame.engine.gameobjects;

import com.badlogic.gdx.math.Vector2;
import com.wisekrakr.firstgame.engine.SpaceEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DodgingEnemy extends Enemy {

    private float DEFAULT_ENEMY_SPEED = 20;
    private static final float AGRO_DISTANCE = 220;
    private static final float ATTACK_DISTANCE = 200;
    private AttackState attackState = AttackState.PACIFIST;

    private float direction;
    private float radius;
    private int ammoCount;
    private float shotLeftOver;

    public DodgingEnemy(String name, Vector2 position, float direction, float radius, SpaceEngine space) {
        super(name, position, direction, radius, space);
        this.direction = direction;
        this.radius = radius;
        ammoCount = (int) Double.POSITIVE_INFINITY;
        setCollisionRadius(radius);
    }

    @Override
    public void signalOutOfBounds(Set<GameObject> toDelete, Set<GameObject> toAdd) {
        super.signalOutOfBounds(toDelete, toAdd);
    }

    @Override
    public void collide(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {

        toDelete.add(subject);

        if (subject instanceof Asteroid) {
            toDelete.add(this);
        }

        if(subject instanceof Bullet){
            toDelete.add(this);
        }

    }


    @Override
    public void targetSpotted(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player) {

            if (distanceBetween(this, subject) <= AGRO_DISTANCE ) {

                float angle = angleBetween(this, subject);

                setPosition(new Vector2(getPosition().x -=  Math.cos(angle) , getPosition().y -=  Math.sin(angle) ));

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

    @Override
    public void nothingSpotted(GameObject subject, Set<GameObject> toDelete, Set<GameObject> toAdd) {
        if (subject instanceof Player) {

            if ((distanceBetween(this, subject) > AGRO_DISTANCE)) {

                attackState = AttackState.PACIFIST;
            }
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
                float shotCount = delta / 0.3f + shotLeftOver;

                int exactShotCount = Math.min(Math.round(shotCount), ammoCount);

                ammoCount = ammoCount - exactShotCount;
                if (ammoCount > 0) {
                    shotLeftOver = shotCount - exactShotCount;
                } else {
                    shotLeftOver = 0;
                }

                for (int i = 0; i < exactShotCount; i++) {
                    toAdd.add(new Bullet("bullito", getPosition(), getSpace(), getOrientation(), 400, 2f));
                }

                break;
            case PACIFIST:
                shotLeftOver = 0;
                break;

        }
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

    public int getAmmoCount() {
        return ammoCount;
    }

    public void setAmmoCount(int ammoCount) {
        this.ammoCount = ammoCount;
    }

    @Override
    public Map<String, Object> getExtraSnapshotProperties() {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("radius", radius);

        return result;
    }

}




